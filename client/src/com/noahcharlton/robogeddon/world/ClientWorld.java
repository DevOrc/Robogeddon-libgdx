package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.client.RemoteServer;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityRemovedMessage;
import com.noahcharlton.robogeddon.entity.EntityUpdateMessage;
import com.noahcharlton.robogeddon.entity.NewEntityMessage;
import com.noahcharlton.robogeddon.message.Message;

public class ClientWorld extends World {

    private final ServerProvider server;

    public ClientWorld() {
        super(false);
        this.server = new RemoteServer();
    }

    public void update(){
        super.update();
    }

    public void updateMessages(){
        Message message;

        while((message = server.getMessageFromServer()) != null){
            onMessageReceived(message);
        }
    }

    protected boolean onMessageReceived(Message message) {
        if(super.onMessageReceived(message)){

        }else if(message instanceof NewEntityMessage){
            spawnEntity((NewEntityMessage) message);
        }else if(message instanceof EntityUpdateMessage) {
            updateEntity((EntityUpdateMessage) message);
        }else if(message instanceof EntityRemovedMessage){
            removeEntity((EntityRemovedMessage) message);
        }else{
            Log.warn("Unknown message type: " + message.getClass());
        }

        return false;
    }

    private void removeEntity(EntityRemovedMessage message) {
        boolean removed = entities.removeIf(e -> e.getId() == message.getID());

        if(!removed){
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
        if(getEntityByID(message.getID()) != null){
            Log.warn("Entity already registered with id: " + message.getID());
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
        for(Entity entity: entities){
            entity.getType().render(batch, entity);
        }
    }

    public ServerProvider getServer() {
        return server;
    }
}
