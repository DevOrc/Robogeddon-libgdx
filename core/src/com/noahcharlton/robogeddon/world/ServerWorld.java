package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityRemovedMessage;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.NewEntityMessage;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.floor.Floors;
import com.noahcharlton.robogeddon.world.gen.WorldGenerator;
import com.noahcharlton.robogeddon.world.item.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Side(Side.SERVER)
public class ServerWorld extends World{

    private final HashMap<Integer, Entity> players = new HashMap<>();
    private final ServerProvider server;
    private int entityID = 0;

    public ServerWorld(ServerProvider server) {
        super(true);
        this.server = server;

        setWidth(100);
        setHeight(100);

        Tile[][] tiles = new Tile[getWidth()][getHeight()];
        for(int x = 0; x < getWidth(); x++){
            for(int y = 0; y < getHeight(); y++){
                tiles[x][y] = new Tile(this, x, y);
                tiles[x][y].setFloor(Floors.dirt, false);
            }
        }
        setTiles(tiles);
        Log.info("World created with size " + getWidth() + "x" + getHeight());
        WorldGenerator.gen(this);
        Log.info("Successfully generated the world!");
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

        for(int x = 0; x < getWidth(); x++){
            for(int y = 0; y < getHeight(); y++){
                var tile = getTileAt(x, y);

                if(tile.isDirty()){
                    dirtyTiles.add(tile);
                    tile.clean();
                }
            }
        }

        if(!dirtyTiles.isEmpty())
            sendMessageToClient(new UpdateWorldMessage(dirtyTiles));
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
        }else{
            Log.warn("Unknown message type: " + message.getClass());
        }

        return false;
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
        sendMessageToClient(new NewEntityMessage(entity.getType().getTypeID(), entity.getId()));

        return entity;
    }

    @Override
    protected void onEntityDead(Entity entity) {
        sendMessageToClient(new EntityRemovedMessage(entity.getId()));
    }

    public void handleNewConnection(int connID) {
        Log.debug("New client: " + connID);

        for(Entity entity : entities){
            server.sendSingle(connID, new NewEntityMessage(entity.getType().getTypeID(), entity.getId()));
        }

        server.sendSingle(connID, inventory.createSyncMessage());
        addNewPlayer(connID);
        //Send world last, because it takes so many messages, that the
        //new player and assign player messages are sent out of order
        sendWorldToClient(connID);
    }

    private void sendWorldToClient(int connID) {
        //Send the client one ylevel per message
        //because of the message byte limit
        for(int y = 0; y < getHeight(); y++){
            server.sendSingle(connID, new WorldSyncMessage(this, y));
        }
    }

    private void addNewPlayer(int connID) {
        var player = addEntity(EntityType.robotEntity.create(this));
        var message = new AssignRobotMessage(player.getId());

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
