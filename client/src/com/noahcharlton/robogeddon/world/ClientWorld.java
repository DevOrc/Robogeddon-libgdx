package com.noahcharlton.robogeddon.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Message;
import com.noahcharlton.robogeddon.client.ServerThread;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityUpdateMessage;
import com.noahcharlton.robogeddon.entity.NewEntityMessage;

public class ClientWorld extends World {

    private final ServerThread server;

    public ClientWorld() {
        super(false);
        this.server = new ServerThread();
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
        }else{
            Log.warn("Unknown message type: " + message.getClass());
        }

        return false;
    }

    private void updateEntity(EntityUpdateMessage message) {
        var entity = getEntityByID(message.getId());
        entity.onUpdateMessage(message);
    }

    private void spawnEntity(NewEntityMessage message) {
        var type = Core.entities.get(message.getEntityType());
        var entity = type.create(this);
        entity.setId(message.getID());

        Log.debug("New Entity: ID=" + entity.getId() + " Type=" + entity.getClass().getName());
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

    public ServerThread getServer() {
        return server;
    }
}
