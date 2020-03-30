package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.client.ClientLauncher;
import com.noahcharlton.robogeddon.client.LocalServer;
import com.noahcharlton.robogeddon.client.RemoteServer;
import com.noahcharlton.robogeddon.entity.*;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.item.InventorySyncMessage;

@Side(Side.CLIENT)
public class ClientWorld extends World {

    private final ServerProvider server;
    private Entity playersRobot;

    public ClientWorld() {
        super(false);

        this.server = ClientLauncher.runLocal ? new LocalServer() : new RemoteServer();
    }

    public void update() {
        super.update();
    }

    public void updateMessages() {
        Message message;

        while((message = server.getMessageFromServer()) != null) {
            onMessageReceived(message);
        }
    }

    protected boolean onMessageReceived(Message message) {
        if(message instanceof AssignRobotMessage){
            playersRobot = getEntityByID(((AssignRobotMessage) message).getID());
        }

        if(super.onMessageReceived(message)) {

        } else if(message instanceof NewEntityMessage) {
            spawnEntity((NewEntityMessage) message);
        } else if(message instanceof EntityUpdateMessage) {
            updateEntity((EntityUpdateMessage) message);
        } else if(message instanceof EntityRemovedMessage) {
            removeEntity((EntityRemovedMessage) message);
        } else if(message instanceof WorldSyncMessage) {
            onWorldSync((WorldSyncMessage) message);
        } else if(message instanceof UpdateWorldMessage){
            updateWorld((UpdateWorldMessage) message);
        } else if(message instanceof InventorySyncMessage){
            syncInventory((InventorySyncMessage) message);
        }else {
            Log.warn("Unknown message type: " + message.getClass());
        }

        return false;
    }

    private void syncInventory(InventorySyncMessage message) {
        for(int i = 0; i < message.getIds().length; i++){
            var item = Core.items.get(message.getIds()[i]);
            var amount = message.getAmounts()[i];

            inventory.setItem(item, amount);
        }
    }

    private void updateWorld(UpdateWorldMessage message) {
        if(getTiles() == null){
            Log.warn("World updated before synced with server??");
            return;
        }

        for(TileUpdate update : message.getUpdates()){
            updateTile(update);
        }
    }

    private void updateTile(TileUpdate update){
        var tile = getTileAt(update.x, update.y);

        if(update.floor == null){
            throw new RuntimeException("Cannot have null floor.");
        }else{
            tile.setFloor(Core.floors.get(update.floor), false);
        }

        if(update.block == null){
            tile.setBlock(null, false);
        }else{
            tile.setBlock(Core.blocks.get(update.block), false);
        }

    }

    private void onWorldSync(WorldSyncMessage message) {
        if(getWidth() == -1) {//This is the first sync message, so we must create the world
            onFirstWorldSync(message);
            return;
        }

        for(int x = 0; x < getWidth(); x++) {
            updateTile(message.getTiles()[x]);
        }
    }

    private void onFirstWorldSync(WorldSyncMessage message) {
        //Note: setWidth, setHeight, and setTiles should throw an exception if they are already set
        this.setWidth(message.getWorldWidth());
        this.setHeight(message.getWorldHeight());

        Tile[][] tiles = new Tile[getWidth()][getHeight()];
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++){
                tiles[x][y] = new Tile(this, x, y);
            }
        }
        setTiles(tiles);
        onWorldSync(message); //Now sync the y-level in this message
        Log.info("Created World! Size = " + getWidth() + "x" + getHeight());
    }

    private void removeEntity(EntityRemovedMessage message) {
        boolean removed = entities.removeIf(e -> e.getId() == message.getID());

        if(!removed) {
            Log.warn("Entity removed that wasn't in the world?? ID=" + message.getID());
        }
    }

    private void updateEntity(EntityUpdateMessage message) {
        var entity = getEntityByID(message.getId());

        if(entity != null)
            entity.onUpdateMessage(message);
    }

    private void spawnEntity(NewEntityMessage message) {
        var type = Core.entities.get(message.getEntityType());

        Log.debug("New Entity: ID=" + message.getID() + " Type=" + type.getClass().getName());
        if(getEntityByID(message.getID()) != null) {
            Log.warn("Entity already registered with id: " + message.getID());
            return;
        }

        var entity = type.create(this);
        entity.setId(message.getID());
        entities.add(entity);
    }

    @Override
    public void sendMessageToServer(Message m) {
        server.sendMessageToServer(m);
    }

    public void render(SpriteBatch batch) {
        for(int x = 0; x < getWidth(); x++) {
            for(int y = 0; y < getHeight(); y++) {
                getTileAt(x, y).render(batch);
            }
        }

        for(Entity entity : entities) {
            entity.getType().render(batch, entity);
        }
    }

    public ServerProvider getServer() {
        return server;
    }

    public Entity getPlayersRobot() {
        return playersRobot;
    }
}
