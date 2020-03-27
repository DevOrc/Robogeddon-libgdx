package com.noahcharlton.robogeddon.client;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Message;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.world.ServerWorld;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerThread implements Runnable, ServerProvider {

    private final Thread thread = new Thread(this, "Server");
    private final Queue<Message> clientToSever = new ConcurrentLinkedQueue<>();
    private final Queue<Message> serverToClient = new ConcurrentLinkedQueue<>();

    private ServerWorld world;

    public ServerThread() {
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        Log.info("Starting local server!");
        world = new ServerWorld(this);

        Core.runServer(world);
    }

    @Override
    public String getName() {
        return "Local Server Thread";
    }

    @Override
    public void sendMessageToClient(Message message) {
        serverToClient.add(message);
    }

    public void sendMessageToServer(Message message){
       clientToSever.add(message);
    }

    @Override
    public Message getMessageFromClient() {
        return clientToSever.poll();
    }

    public Message getMessageFromServer(){
        return serverToClient.poll();
    }
}
