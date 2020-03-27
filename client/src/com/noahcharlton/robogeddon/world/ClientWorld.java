package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Message;
import com.noahcharlton.robogeddon.client.ServerThread;
import com.noahcharlton.robogeddon.entity.EntityMessage;

public class ClientWorld extends World {

    private final ServerThread server;

    public ClientWorld() {
        this.server = new ServerThread();
    }

    public void fixedUpdate(){

    }

    public void updateMessages(){
        Message message;

        while((message = server.getMessageFromServer()) != null){
            onMessageReceived(message);
        }
    }

    private void onMessageReceived(Message message) {
        Log.trace("Received message from server: " + message);

        if(message instanceof EntityMessage){
            spawnEntity((EntityMessage) message);
        }else{
            Log.warn("Unknown message type: " + message.getClass());
        }
    }

    private void spawnEntity(EntityMessage message) {
        var type = Core.entities.get(message.getEntityType());
        var entity = type.create(this);
        entity.setId(message.getID());

        Log.debug("New Entity: ID=" + entity.getId() + " Type=" + entity.getClass().getName());
        entities.add(entity);
    }

    public ServerThread getServer() {
        return server;
    }
}
