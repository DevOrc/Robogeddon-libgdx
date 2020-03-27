package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Message;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.NewEntityMessage;
import com.noahcharlton.robogeddon.entity.EntityType;

public class ServerWorld extends World{

    private final ServerProvider server;
    private int entityID = 0;

    public ServerWorld(ServerProvider server) {
        super(true);
        this.server = server;

        addEntity(EntityType.robotEntity.create(this));
    }

    public void fixedUpdate(){
        super.fixedUpdate();
    }

    public void addEntity(Entity entity){
        if(entity.getId() != Entity.DEFAULT_ID){
           throw new IllegalStateException("Entity already has an ID: " + entity.getId());
        }

        entity.setId(++entityID);
        entities.add(entity);

        Log.debug("New Entity: ID=" + entity.getId() + " Type=" + entity.getClass().getName());
        sendMessageToClient(new NewEntityMessage(entity.getType().getTypeID(), entity.getId()));
    }

    @Override
    public void sendMessageToClient(Message message){
        Log.trace("Sending message to client: " + message);
        server.sendMessageToClient(message);
    }

    public ServerProvider getServer() {
        return server;
    }
}
