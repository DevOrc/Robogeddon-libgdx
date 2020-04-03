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
        Chunk chunk = new Chunk(this, message);
        chunks.put(message.getChunk(), chunk);
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
        chunks.values().forEach(chunk -> chunk.render(batch));

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
