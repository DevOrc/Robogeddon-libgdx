package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.entity.CustomEntityMessage;
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

    protected boolean onMessageReceived(Message message){
        if(message instanceof CustomEntityMessage){
            onCustomEntityMessage((CustomEntityMessage) message);
        }else{
            return false;
        }

        return true;
    }

    private void onCustomEntityMessage(CustomEntityMessage message) {
        var entity = getEntityByID(message.getID());

        if(entity == null){
            Log.warn("Message sent for " + message.getID() +" of type " + message.getClass().getName());
        }else{
            getEntityByID(message.getID()).onCustomMessageReceived(message);
        }
    }

    public void update(){
        entities.forEach(Entity::onUpdate);
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
