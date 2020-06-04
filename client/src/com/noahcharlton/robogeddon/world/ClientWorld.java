package com.noahcharlton.robogeddon.world;import com.badlogic.gdx.Gdx;import com.badlogic.gdx.graphics.Color;import com.badlogic.gdx.graphics.g2d.SpriteBatch;import com.noahcharlton.robogeddon.Core;import com.noahcharlton.robogeddon.ServerProvider;import com.noahcharlton.robogeddon.block.tileentity.UpdateTileEntitiesMessage;import com.noahcharlton.robogeddon.block.tileentity.fluid.FluidBuffer;import com.noahcharlton.robogeddon.block.tileentity.fluid.HasFluid;import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;import com.noahcharlton.robogeddon.client.Chat;import com.noahcharlton.robogeddon.client.GameClient;import com.noahcharlton.robogeddon.client.LocalServer;import com.noahcharlton.robogeddon.client.MainMenuServer;import com.noahcharlton.robogeddon.client.RemoteServer;import com.noahcharlton.robogeddon.entity.Entity;import com.noahcharlton.robogeddon.entity.EntityRemovedMessage;import com.noahcharlton.robogeddon.entity.EntityUpdateMessage;import com.noahcharlton.robogeddon.entity.NewEntityMessage;import com.noahcharlton.robogeddon.graphics.GameRenderer;import com.noahcharlton.robogeddon.message.ChatMessage;import com.noahcharlton.robogeddon.message.Message;import com.noahcharlton.robogeddon.message.PauseGameMessage;import com.noahcharlton.robogeddon.util.Side;import com.noahcharlton.robogeddon.util.log.Log;import com.noahcharlton.robogeddon.util.shader.BorderShader;import com.noahcharlton.robogeddon.world.electricity.ClientPowerGraph;import com.noahcharlton.robogeddon.world.electricity.PowerGraphUpdate;import com.noahcharlton.robogeddon.world.item.InventorySyncMessage;import com.noahcharlton.robogeddon.world.settings.NewWorldSettings;import com.noahcharlton.robogeddon.world.settings.RemoteWorldSettings;import com.noahcharlton.robogeddon.world.settings.SavedWorldSettings;import com.noahcharlton.robogeddon.world.settings.WorldSettings;import com.noahcharlton.robogeddon.world.team.Team;import java.util.ArrayList;import java.util.Collections;import java.util.Iterator;import java.util.List;import java.util.Map;@Side(Side.CLIENT)public class ClientWorld extends World {    private final ServerProvider server;    private final Chat chat = new Chat();    private Map<Team, ClientPowerGraph> powerGraphs = Collections.emptyMap();    private Entity playersRobot;    public ClientWorld(WorldSettings settings) {        super(false);        if(settings instanceof MainMenuWorld.MainMenuWorldSettings){            server = new MainMenuServer(settings);        }else if(settings instanceof NewWorldSettings || settings instanceof SavedWorldSettings){            server = new LocalServer(settings);        }else if(settings instanceof RemoteWorldSettings){            server = new RemoteServer((RemoteWorldSettings) settings);        }else{            throw new IllegalArgumentException("Unknown settings: " + settings);        }    }    public void shutdown(){        server.getThread().interrupt();    }    public void update() {        if(paused)            return;        super.update();    }    public void updateMessages() {        Message message;        while((message = server.getMessageFromServer()) != null) {            onMessageReceived(message);        }    }    protected boolean onMessageReceived(Message message) {        if(message instanceof AssignRobotMessage) {            playersRobot = getEntityByID(((AssignRobotMessage) message).getID());        }        if(super.onMessageReceived(message)) {        } else if(message instanceof NewEntityMessage) {            spawnEntity((NewEntityMessage) message);        } else if(message instanceof EntityUpdateMessage) {            updateEntity((EntityUpdateMessage) message);        } else if(message instanceof EntityRemovedMessage) {            removeEntity((EntityRemovedMessage) message);        } else if(message instanceof WorldSyncMessage) {            onWorldSync((WorldSyncMessage) message);        } else if(message instanceof UpdateWorldMessage) {            updateWorld((UpdateWorldMessage) message);        } else if(message instanceof InventorySyncMessage) {            syncInventory((InventorySyncMessage) message);        } else if(message instanceof UpdateTileEntitiesMessage) {            updateTileEntities((UpdateTileEntitiesMessage) message);        } else if(message instanceof ChunkTeamUpdateMessage) {            updateChunkTeam((ChunkTeamUpdateMessage) message);        } else if(message instanceof PauseGameMessage) {            paused = ((PauseGameMessage) message).isPaused();        } else if(message instanceof PowerGraphUpdate) {            powerGraphs = ((PowerGraphUpdate) message).getPowerGraphs();        } else if(message instanceof ChatMessage) {            chat.handleMessage((ChatMessage) message);        } else if(message instanceof UnlockedBlockMessage) {            unlockedBlocks.add(((UnlockedBlockMessage) message).getBlock());        } else {            Log.warn("Unknown message type: " + message.getClass());        }        return false;    }    private void updateChunkTeam(ChunkTeamUpdateMessage message) {        Chunk chunk = getChunkAt(message.getChunkX(), message.getChunkY());        if(chunk != null) //Chunk might be null if the server hasn't sent the chunk yet            chunk.setTeam(message.getNewTeam());    }    private void updateTileEntities(UpdateTileEntitiesMessage message) {        for(var update : message.getUpdates()){            var tile = getTileAt(update.tileX, update.tileY);            var tileEntity = tile.getTileEntity();            if(tileEntity instanceof HasInventory){                var buffers = update.items;                var inventory = (HasInventory) tileEntity;                if(buffers != null)                    inventory.setBuffers(buffers.toArray(new ItemBuffer[0]));            }            if(tileEntity instanceof HasFluid){                var buffers = update.fluids;                var inventory = (HasFluid) tileEntity;                if(buffers != null)                    inventory.setFluidBuffers(buffers.toArray(new FluidBuffer[0]));            }            tileEntity.receiveData(update.data);        }    }    private void syncInventory(InventorySyncMessage message) {        for(int i = 0; i < message.getIds().length; i++){            var item = Core.items.get(message.getIds()[i]);            var amount = message.getAmounts()[i];            inventory.setItem(item, amount);        }    }    private void updateWorld(UpdateWorldMessage message) {        for(TileUpdate update : message.getUpdates()){            updateTile(update);        }    }    private void updateTile(TileUpdate update){        var tile = getTileAt(update.x, update.y);        if(tile == null){            Log.warn("Updated tile that does not exist: (" + update.x +", " + update.y +")");        }else{            tile.onTileUpdate(update);        }    }    private void onWorldSync(WorldSyncMessage message) {        Chunk chunk = new Chunk(this, message);        chunks.put(message.getChunk(), chunk);    }    private void removeEntity(EntityRemovedMessage message) {        for(Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {            Entity entity = it.next();            if(entity.getId() == message.getID()){                it.remove();                entity.setDead(true);                return;            }        }        Log.warn("Entity removed that wasn't in the world?? ID=" + message.getID());    }    private void updateEntity(EntityUpdateMessage message) {        var entity = getEntityByID(message.getId());        if(entity != null)            entity.onUpdateMessage(message);    }    private void spawnEntity(NewEntityMessage message) {        var type = Core.entities.get(message.getEntityType());        Log.debug("New Entity: ID=" + message.getID() + " Type=" + type.getClass().getName());        if(getEntityByID(message.getID()) != null) {            Log.warn("Entity already registered with id: " + message.getID());            return;        }        var entity = type.create(this, message.getTeam());        entity.setId(message.getID());        entities.add(entity);    }    @Override    public void sendMessageToServer(Message m) {        server.sendMessageToServer(m);    }    public void render(SpriteBatch batch) {        var renderChunks = playersRobot == null ? chunks.values() : getRenderChunks();        renderChunks.forEach(chunk -> chunk.renderFloors(batch));        renderChunks.forEach(chunk -> chunk.renderBlocks(batch, 0));        renderChunks.forEach(chunk -> chunk.renderBlocks(batch, 1));        renderChunks.forEach(chunk -> chunk.renderBlocks(batch, 2));        batch.end();        BorderShader.getProgram().begin();        renderChunks.forEach(Chunk::renderTeam);        BorderShader.getProgram().end();        batch.begin();        for(Entity entity : entities) {            entity.getType().render(batch, entity);            batch.setColor(Color.WHITE);        }    }    List<Chunk> getRenderChunks() {        GameRenderer renderer = GameClient.getInstance().getRenderer();        var chunkX = Math.floorDiv((int) playersRobot.getX(), Chunk.SIZE * Tile.SIZE);        var chunkY = Math.floorDiv((int) playersRobot.getY(), Chunk.SIZE * Tile.SIZE);        List<Chunk> chunks = new ArrayList<>();        float halfWidth = renderer.getCamera().zoom * Gdx.graphics.getWidth() / 2f;        float halfHeight = renderer.getCamera().zoom * Gdx.graphics.getHeight() / 2f;        int renderWidth = (int) Math.ceil(halfWidth / (Chunk.SIZE * Tile.SIZE)) + 1;        int renderHeight = (int) Math.ceil(halfHeight / (Chunk.SIZE * Tile.SIZE)) + 1;        for(int x = chunkX - renderWidth; x <= chunkX + renderWidth; x++){            for(int y = chunkY - renderHeight; y <= chunkY + renderHeight; y++){                var chunk = getChunkAt(x, y);                if(chunk != null)                    chunks.add(chunk);            }        }        return chunks;    }    public ServerProvider getServer() {        return server;    }    public Entity getPlayersRobot() {        return playersRobot;    }    public ClientPowerGraph getPowerForTeam(Team team) {        return powerGraphs.get(team);    }    public Chat getChat() {        return chat;    }    @Override    public String toString() {        return "ClientWorld";    }}