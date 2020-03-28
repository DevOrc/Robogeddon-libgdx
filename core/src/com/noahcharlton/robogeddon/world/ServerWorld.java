package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityRemovedMessage;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.NewEntityMessage;
import com.noahcharlton.robogeddon.message.Message;

import java.util.HashMap;

public class ServerWorld extends World{

    private final HashMap<Integer, Entity> players = new HashMap<>();
    private final ServerProvider server;
    private int entityID = 0;

    public ServerWorld(ServerProvider server) {
        super(true);
        this.server = server;
    }

    public void update(){
        super.update();
    }

    public void updateMessages(){
        Message message;
        while((message = server.getMessageFromClient()) != null){
            onMessageReceived(message);
        }
    }

    protected boolean onMessageReceived(Message message) {
        if(super.onMessageReceived(message)){

        }else if(message instanceof LostClientMessage){
            onClientLost((LostClientMessage) message);
        }else{
            Log.warn("Unknown message type: " + message.getClass());
        }

        return false;
    }

    private void onClientLost(LostClientMessage message) {
        var entity = players.get(message.getConnectionID());

        if(entity == null){
            throw new RuntimeException("Lost client without a player??? ID=" + message.getConnectionID());
        }else{
            entity.setDead(true);
        }
    }

    public Entity addEntity(Entity entity){
        if(entity.getId() != Entity.DEFAULT_ID){
           throw new IllegalStateException("Entity already has an ID: " + entity.getId());
        }

        entity.setId(++entityID);
        entities.add(entity);

        Log.debug("New Entity: ID=" + entity.getId() + " Type=" + entity.getClass().getName());
        sendMessageToClient(new NewEntityMessage(entity.getType().getTypeID(), entity.getId()));

        return entity;
    }

    @Override
    protected void onEntityDead(Entity entity) {
        sendMessageToClient(new EntityRemovedMessage(entity.getId()));
    }

    public void handleNewConnection(int connID) {
        Log.debug("New client: " + connID);
        for(Entity entity : entities){
            server.sendSingle(connID, new NewEntityMessage(entity.getType().getTypeID(), entity.getId()));
        }

        addNewPlayer(connID);
    }

    private void addNewPlayer(int connID) {
        var player = addEntity(EntityType.robotEntity.create(this));
        var message = new AssignRobotMessage(player.getId());

        players.put(connID, player);

        Server.runLater(() -> server.sendSingle(connID, message));
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
