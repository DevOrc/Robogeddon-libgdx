package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.entity.CustomEntityMessage;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.message.Message;

import java.util.Iterator;
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
            Log.warn("Non-existent entity " + message.getID() + " sent message of type "
                    + message.getClass().getName());
        }else{
            getEntityByID(message.getID()).onCustomMessageReceived(message);
        }
    }

    public void update(){
        entities.forEach(Entity::onUpdate);

        for(Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();

            if(entity.isDead()){
                it.remove();
                onEntityDead(entity);
            }
        }
    }

    protected void onEntityDead(Entity entity) {
        throw new UnsupportedOperationException("Only the server can have dead entities");
    }

    private void sendMessageToOppositeSide(Message message){
        if(isServer){
            sendMessageToClient(message);
        }else{
            sendMessageToServer(message);
        }
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
