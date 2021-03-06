package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.math.GridPoint2;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockGroup;
import com.noahcharlton.robogeddon.block.Multiblock;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.UpdateTileEntitiesMessage;
import com.noahcharlton.robogeddon.command.Commands;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityRemovedMessage;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.NewEntityMessage;
import com.noahcharlton.robogeddon.message.ChatMessage;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.message.PauseGameMessage;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.world.electricity.PowerGraphUpdate;
import com.noahcharlton.robogeddon.world.electricity.ServerPowerGraph;
import com.noahcharlton.robogeddon.world.gen.TutorialWorldGenerator;
import com.noahcharlton.robogeddon.world.gen.WorldGenerator;
import com.noahcharlton.robogeddon.world.io.SaveWorldMessage;
import com.noahcharlton.robogeddon.world.io.WorldIO;
import com.noahcharlton.robogeddon.world.item.Inventory;
import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.settings.NewWorldSettings;
import com.noahcharlton.robogeddon.world.settings.SavedWorldSettings;
import com.noahcharlton.robogeddon.world.settings.TutorialWorldSettings;
import com.noahcharlton.robogeddon.world.settings.WorldSettings;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Side(Side.SERVER)
public class ServerWorld extends World {

    private final HashMap<Integer, Entity> players = new HashMap<>();
    private final EnumMap<Team, ServerPowerGraph> powerGraphs = new EnumMap<>(Team.class);
    private final ServerProvider server;

    private final Team playerTeam = Team.BLUE;
    private final Team enemyTeam = Team.RED;

    private TutorialHandler tutorialHandler;
    private WorldGenerator generator;
    private int lastEntityID = 0;

    private int powerUpdateTick = 0;

    public ServerWorld(ServerProvider server, WorldSettings settings) {
        super(true);

        this.server = server;
        Arrays.stream(Team.values()).forEach(team -> powerGraphs.put(team, new ServerPowerGraph()));

        if(settings instanceof SavedWorldSettings) {
            WorldIO.load(this, (SavedWorldSettings) settings);
        } else if(settings instanceof NewWorldSettings) {
            generator = ((NewWorldSettings) settings).createGenerator();
            generator.createInitialWorld(this);
        } else if(settings instanceof TutorialWorldSettings){
            generator = new TutorialWorldGenerator();
            generator.createInitialWorld(this);
            tutorialHandler = new TutorialHandler(this);
        }else {
            throw new IllegalArgumentException("Unknown settings: " + settings);
        }

        Log.info("Successfully generated the world!");
    }

    public Chunk createChunk(int x, int y, boolean generate) {
        if(getChunkAt(x, y) != null)
            throw new RuntimeException("Cannot override a chunk!");

        var location = new GridPoint2(x, y);
        Chunk chunk = new Chunk(this, location);
        chunk.setTeam(Team.NEUTRAL);

        if(generate)
            generator.genChunk(chunk);

        sendMessageToClient(new WorldSyncMessage(chunk));
        chunks.put(location, chunk);
        Log.trace("Created Chunk at " + location);

        return chunk;
    }

    public void update() {
        if(paused)
            return;
        super.update();

        if(tutorialHandler != null){
            tutorialHandler.update();
        }else{
            updateUnlockedBlocks();
        }

        if(inventory.isDirty()) {
            inventory.clean();
            sendMessageToClient(inventory.createSyncMessage());
        }

        updatePowerGraphs();
        createPlayerChunkBuffer();
        sendDirtyTiles();
    }

    private void updateUnlockedBlocks() {
        for(BlockGroup group : Core.blockGroups.values()){
            for(Block block : group.getBlocks()){
                if(!unlockedBlocks.contains(block, true) && canBeUnlocked(block)){
                    unlockBlock(block);
                }
            }
        }
    }

    private boolean canBeUnlocked(Block block) {
        for(Block requiredBlock : block.getRequiredBlocks()){
            if(!unlockedBlocks.contains(requiredBlock, true)){
                return false;
            }
        }

        for(ItemStack requirement : block.getRequirements()){
            if(requirement.getAmount() > getInventoryForItem(requirement.getItem())){
                return false;
            }
        }

        return true;
    }

    public void updatePowerGraphs() {
        powerGraphs.values().forEach(ServerPowerGraph::update);

        powerUpdateTick++;

        if(powerUpdateTick > 60){
            powerUpdateTick = 0;
            sendMessageToClient(new PowerGraphUpdate(powerGraphs));
        }
    }

    private void createPlayerChunkBuffer() {
        for(Chunk chunk : chunks.values()) {
            if(chunk.getTeam() == playerTeam) {
                boolean chunkOnEdge = chunk.getNeighborLocations().stream()
                        .map(pt -> getChunkAt(pt.x, pt.y)).anyMatch(Objects::isNull);

                if(chunkOnEdge) {
                    generator.genChunksAround(this, chunk);
                }
            }
        }
    }

    private void sendDirtyTiles() {
        List<Tile> dirtyTiles = new ArrayList<>();
        List<TileEntity> dirtyTileEntities = new ArrayList<>();
        chunks.values().forEach(chunk -> getDirtyTilesInChunk(dirtyTiles, dirtyTileEntities, chunk));

        if(!dirtyTiles.isEmpty()) {
            sendMessageToClient(new UpdateWorldMessage(dirtyTiles));
        }

        if(!dirtyTileEntities.isEmpty()) {
            sendMessageToClient(new UpdateTileEntitiesMessage(dirtyTileEntities));
        }
    }

    private void getDirtyTilesInChunk(List<Tile> dirtyTiles, List<TileEntity> dirtyTileEntities, Chunk chunk) {
        for(int x = 0; x < Chunk.SIZE; x++) {
            for(int y = 0; y < Chunk.SIZE; y++) {
                var tile = chunk.getTile(x, y);

                if(tile.isDirty()) {
                    dirtyTiles.add(tile);
                    tile.clean();
                }

                if(tile.getTileEntity() != null && tile.getTileEntity().isDirty()) {
                    dirtyTileEntities.add(tile.getTileEntity());
                    tile.getTileEntity().clean();
                }
            }
        }
    }

    public void updateMessages() {
        Message message;
        while((message = server.getMessageFromClient()) != null) {
            onMessageReceived(message);
        }
    }

    protected boolean onMessageReceived(Message message) {
        if(super.onMessageReceived(message)) {

        } else if(message instanceof LostClientMessage) {
            onClientLost((LostClientMessage) message);
        } else if(message instanceof BuildBlockMessage) {
            onBuildBlockRequest((BuildBlockMessage) message);
        } else if(message instanceof SaveWorldMessage && !server.isRemote()) {
            WorldIO.save(this, ((SaveWorldMessage) message).getPath());
        } else if(message instanceof PauseGameMessage) {
            updatePausedState((PauseGameMessage) message);
        } else if(message instanceof ChatMessage){
            handleChat((ChatMessage) message);
        }else {
            Log.warn("Unknown message type: " + message.getClass());
        }

        return false;
    }

    private void handleChat(ChatMessage message) {
        var text = message.getText();

        if(text.startsWith("/")){
            Commands.handle(this, text);
        }else{
            Log.info("[CHAT]: " + message.getText());
            sendMessageToClient(message);
        }
    }

    private void updatePausedState(PauseGameMessage message) {
        if(server.isRemote()) //a multiplayer server never pauses!
            return;

        paused = message.isPaused();
        Log.info("Server Paused: " + paused);
        sendMessageToClient(new PauseGameMessage(paused));
    }

    private void onBuildBlockRequest(BuildBlockMessage message) {
        Tile tile = getTileAt(message.getTileX(), message.getTileY());
        Block block = Core.blocks.getOrNull(message.getBlockID());
        var entity = getEntityByID(message.getPlayerID());

        if(entity == null || entity.isDead())
            return;

        if(block == null && tile.hasBlock() && teamCanEditTile(entity.getTeam(), tile)) {
            destroyBlock(tile);
        } else if(block != null && canBuildBlock(entity, block, tile)) {
            buildBlock(tile, block);
        }
    }

    public void destroyBlock(Tile tile) {
        Block block = tile.getBlock();

        if(block instanceof Multiblock) {
            var multiBlock = (Multiblock) block;
            destroyBlock(getTileAt(multiBlock.getRootX(), multiBlock.getRootY()));
            return;
        }

        for(int x = tile.getX(); x < tile.getX() + block.getWidth(); x++) {
            for(int y = tile.getY(); y < tile.getY() + block.getHeight(); y++) {
                getTileAt(x, y).setBlock(null, true);
            }
        }
    }

    private boolean canBuildBlock(Entity builder, Block block, Tile root) {
        for(ItemStack requirement : block.getRequirements()) {
            if(getInventoryForItem(requirement.getItem()) < requirement.getAmount()) {
                return false;
            }
        }

        for(int x = root.getX(); x < root.getX() + block.getWidth(); x++) {
            for(int y = root.getY(); y < root.getY() + block.getHeight(); y++) {
                var tile = getTileAt(x, y);

                if(tile.hasBlock() || !block.canBuildAt(tile, builder))
                    return false;
            }
        }

        return true;
    }

    public void buildBlock(Tile tile, Block block) {
        if(tile == null) {
            Log.warn("Cannot build on null tile!!");
            return;
        }

        for(ItemStack requirement : block.getRequirements()) {
            inventory.changeItem(requirement.getItem(), -requirement.getAmount());
        }

        for(int x = tile.getX(); x < tile.getX() + block.getWidth(); x++) {
            for(int y = tile.getY(); y < tile.getY() + block.getHeight(); y++) {
                if(x == tile.getX() && y == tile.getY()) {
                    getTileAt(x, y).setBlock(block, true);
                } else {
                    getTileAt(x, y).setBlock(new Multiblock(block, tile.getX(), tile.getY()), true);
                }
            }
        }
    }

    private boolean teamCanEditTile(Team team, Tile tile) {
        return team == tile.getChunk().getTeam() || tile.getChunk().getTeam() == Team.NEUTRAL;
    }

    private void onClientLost(LostClientMessage message) {
        var entity = players.get(message.getConnectionID());

        if(entity == null) {
            throw new RuntimeException("Lost client without a player??? ID=" + message.getConnectionID());
        } else {
            entity.setDead(true);
        }
    }

    public Entity addEntity(Entity entity) {
        if(entity.getId() != Entity.DEFAULT_ID) {
            if(getEntityByID(entity.getId()) != null)
                throw new IllegalStateException("Duplicate Entity ID: " + entity.getId());
        } else {
            entity.setId(++lastEntityID);
        }

        entities.add(entity);

        Log.debug("New Entity: ID=" + entity.getId() + " Type=" + entity.getClass().getName());
        sendMessageToClient(new NewEntityMessage(entity));

        return entity;
    }

    @Override
    protected void onEntityDead(Entity entity) {
        sendMessageToClient(new EntityRemovedMessage(entity.getId()));
    }

    public void handleNewConnection(int connID) {
        Log.debug("New client: " + connID);

        for(Entity entity : entities) {
            server.sendSingle(connID, new NewEntityMessage(entity));
        }

        server.sendSingle(connID, inventory.createSyncMessage());
        addNewPlayer(connID);
        //Send world last, because it takes so many messages, that the
        //new player and assign player messages are sent out of order
        sendWorldToClient(connID);
    }

    private void sendWorldToClient(int connID) {
        Log.debug("Sending world to new client!");
        chunks.values().forEach(chunk -> server.sendSingle(connID, new WorldSyncMessage(chunk)));
    }

    private void addNewPlayer(int connID) {
        var player = addEntity(EntityType.robotEntity.create(this, playerTeam));
        var message = new AssignRobotMessage(player.getId());

        //Send a single message to the client
        //because sometimes the non-single messages get sent after
        //and then the player is assigned an entity that does not exist yet
        server.sendSingle(connID, new NewEntityMessage(player));
        players.put(connID, player);

        unlockedBlocks.forEach(block -> server.sendSingle(connID, new UnlockedBlockMessage(block)));
        Server.runLater(() -> server.sendSingle(connID, message));
    }

    public void unlockBlock(Block block) {
        unlockBlock(block, true);
    }

    public void unlockBlock(Block block, boolean announce) {
        unlockedBlocks.add(block);
        sendMessageToClient(new UnlockedBlockMessage(block));

        if(announce)
            handleChat(new ChatMessage("Unlocked Block " + block.getDisplayName()));
    }

    @Override
    public void sendMessageToClient(Message message) {
        Log.trace("Sending message to client: " + message);
        server.sendMessageToClient(message);
    }

    public ServerPowerGraph getPowerForTeam(Team team){
        return powerGraphs.get(team);
    }

    public void setGenerator(WorldGenerator generator) {
        if(this.generator != null)
            throw new UnsupportedOperationException("Generator already set!");

        this.generator = generator;
    }

    public ServerProvider getServer() {
        return server;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Iterator<Chunk> getChunks() {
        return chunks.values().iterator();
    }

    public void setLastEntityID(int lastEntityID) {
        this.lastEntityID = lastEntityID;
    }

    public int getLastEntityID() {
        return lastEntityID;
    }

    public Team getEnemyTeam() {
        return enemyTeam;
    }

    public Team getPlayerTeam() {
        return playerTeam;
    }

    public HashMap<Integer, Entity> getPlayers() {
        return players;
    }

    public WorldGenerator getGenerator() {
        return generator;
    }
}
