package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Message;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityMessage;
import com.noahcharlton.robogeddon.entity.EntityType;

public class ServerWorld extends World{

    private final ServerProvider server;
    private int entityID = 0;

    public ServerWorld(ServerProvider server) {
        this.server = server;

        addEntity(EntityType.testEntity.create(this));
    }

    public void update(){

    }

    public void addEntity(Entity entity){
        if(entity.getId() != Entity.DEFAULT_ID){
           throw new IllegalStateException("Entity already has an ID: " + entity.getId());
        }

        entity.setId(++entityID);

        Log.debug("New Entity: ID=" + entity.getId() + " Type=" + entity.getClass().getName());
        sendMessage(new EntityMessage(entity.getType().getTypeID(), entity.getId()));
    }

    public void sendMessage(Message message){
        Log.trace("Sending message to client: " + message);
        server.sendMessageToClient(message);
    }

    public ServerProvider getServer() {
        return server;
    }
}
