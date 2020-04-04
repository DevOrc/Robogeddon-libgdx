package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.math.GridPoint2;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.entity.*;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.gen.WorldGenerator;
import com.noahcharlton.robogeddon.world.item.Inventory;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Side(Side.SERVER)
public class ServerWorld extends World{

    private final WorldGenerator generator = new WorldGenerator(WorldGenerator.seed);
    private final HashMap<Integer, Entity> players = new HashMap<>();
    private final ServerProvider server;

    private int entityID = 0;

    public ServerWorld(ServerProvider server) {
        super(true);
        this.server = server;

        Random random = new Random();
        for(int x = -2; x <= 2; x++){
            for(int y = -2; y <= 2; y++){
                var team = random.nextBoolean() ? Team.BLUE : Team.RED;
                createChunk(x, y, team);
            }
        }
        getChunkAt(0, 0).setTeam(Team.NEUTRAL);

        addEntity(EntityType.droneEntity.create(this, Team.RED));
        Log.info("Successfully generated the world!");
    }

    private void createChunk(int x, int y, Team team) {
        if(getChunkAt(x, y) != null)
            throw new RuntimeException("Cannot override a chunk!");

        var location = new GridPoint2(x, y);
        Chunk chunk = new Chunk(this, location);
        chunk.setTeam(team);
        generator.genChunk(chunk);
        sendMessageToClient(new WorldSyncMessage(chunk));

        chunks.put(location, chunk);
        Log.debug("Created Chunk for " + location + " with team " + team);
    }

    public void update(){
        super.update();

        var tile = getTileAt(0, 0);
        if(System.currentTimeMillis() % 2000 < 1000 && tile.hasBlock()){
            tile.setBlock(null, true);
        }else if(System.currentTimeMillis() % 2000 > 1000 && !tile.hasBlock()){
            tile.setBlock(Blocks.testBlock, true);
        }

        if(inventory.isDirty()){
            inventory.clean();
            sendMessageToClient(inventory.createSyncMessage());
        }

        sendDirtyTiles();
    }

    private void sendDirtyTiles() {
        List<Tile> dirtyTiles = new ArrayList<>();

        chunks.values().forEach(chunk -> getDirtyTilesInChunk(dirtyTiles, chunk));

        if(!dirtyTiles.isEmpty())
            sendMessageToClient(new UpdateWorldMessage(dirtyTiles));
    }

    private void getDirtyTilesInChunk(List<Tile> dirtyTiles, Chunk chunk) {
        if(chunk.isDirty()){
            for(int x = 0; x < Chunk.SIZE; x++){
                for(int y = 0; y < Chunk.SIZE; y++){
                    var tile = chunk.getTile(x, y);
                    if(tile.isDirty()){
                        dirtyTiles.add(tile);
                        tile.clean();
                    }
                }
            }
        }
        chunk.clean();
    }

    public void updateMessages(){
        Message message;
        while((message = server.getMessageFromClient()) != null){
            onMessageReceived(message);
        }
    }

    protected boolean onMessageReceived(Message message) {
        if(super.onMessageReceived(message)){

        }else if(message instanceof LostClientMessage){
            onClientLost((LostClientMessage) message);
        }else if(message instanceof BuildBlockMessage){
            attemptToBuildBlock((BuildBlockMessage) message);
        }else{
            Log.warn("Unknown message type: " + message.getClass());
        }

        return false;
    }

    private void attemptToBuildBlock(BuildBlockMessage message) {
        Tile tile = getTileAt(message.getTileX(), message.getTileY());
        Block block = Core.blocks.getOrNull(message.getBlockID());
        var entity = getEntityByID(message.getPlayerID());

        boolean hasPermission = entity.getTeam() == tile.getChunk().getTeam()
                || tile.getChunk().getTeam() == Team.NEUTRAL;
        if(entity.isDead() || !hasPermission){
            Log.debug("Cannot build block 1");
            return;
        }

        if(tile.hasBlock() && message.getBlockID() != null)
            return;

        tile.setBlock(block, true);
    }

    private void onClientLost(LostClientMessage message) {
        var entity = players.get(message.getConnectionID());

        if(entity == null){
            throw new RuntimeException("Lost client without a player??? ID=" + message.getConnectionID());
        }else{
            entity.setDead(true);
        }
    }

    public Entity addEntity(Entity entity){
        if(entity.getId() != Entity.DEFAULT_ID){
           throw new IllegalStateException("Entity already has an ID: " + entity.getId());
        }

        entity.setId(++entityID);
        entities.add(entity);

        Log.debug("New Entity: ID=" + entity.getId() + " Type=" + entity.getClass().getName());
        sendMessageToClient(new NewEntityMessage(entity));

        return entity;
    }

    @Override
    protected void onEntityDead(Entity entity) {
        if(entity instanceof DroneEntity){
            createChunk(-3, 0, Team.RED);
            createChunk(-3, -1, Team.RED);
            createChunk(-3, 1, Team.RED);
        }
        sendMessageToClient(new EntityRemovedMessage(entity.getId()));
    }

    public void handleNewConnection(int connID) {
        Log.debug("New client: " + connID);

        for(Entity entity : entities){
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
        var player = addEntity(EntityType.robotEntity.create(this, Team.BLUE));
        var message = new AssignRobotMessage(player.getId());

        //Send a single message to the client
        //because sometimes the non-single messages get sent after
        //and then the player is assigned an entity that does not exist yet
        server.sendSingle(connID, new NewEntityMessage(player));
        players.put(connID, player);

        Server.runLater(() -> server.sendSingle(connID, message));
    }

    @Override
    public void sendMessageToClient(Message message){
        Log.trace("Sending message to client: " + message);
        server.sendMessageToClient(message);
    }

    public ServerProvider getServer() {
        return server;
    }

    public Inventory getInventory(){
        return inventory;
    }
}
