package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.Message;
import com.noahcharlton.robogeddon.entity.Entity;

import java.util.LinkedList;
import java.util.List;

public abstract class World {

    private final boolean isServer;
    protected final List<Entity> entities = new LinkedList<>();

    World(boolean isServer) {
        this.isServer = isServer;
    }

    protected Entity getEntityByID(int id){
        for(Entity entity: entities){
            if(entity.getId() == id){
                return entity;
            }
        }

        return null;
    }

    public void fixedUpdate(){
        entities.forEach(Entity::fixedUpdate);
        entities.removeIf(Entity::isDead);
    }

    public void sendMessageToClient(Message m){
        throw new UnsupportedOperationException();
    }

    public void sendMessageToServer(Message m){
        throw new UnsupportedOperationException();
    }

    public boolean isServer() {
        return isServer;
    }

    public boolean isClient(){
        return !isServer;
    }
}
