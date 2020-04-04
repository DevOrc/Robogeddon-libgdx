package com.noahcharlton.robogeddon;

import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.util.Side;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class ServerProvider implements Runnable{

    private final Thread thread = new Thread(this, "Server");
    private final Queue<Message> clientToServer = new ConcurrentLinkedQueue<>();
    private final Queue<Message> serverToClient = new ConcurrentLinkedQueue<>();

    public ServerProvider() {
        thread.setDaemon(true);
        thread.start();
    }

    protected abstract String getName();

    public void sendMessageToClient(Message message) {
        serverToClient.add(message);
    }

    public void sendMessageToServer(Message message){
        clientToServer.add(message);
    }

    public Message getMessageFromClient() {
        return clientToServer.poll();
    }

    public Message getMessageFromServer(){
        return serverToClient.poll();
    }

    @Side(Side.SERVER)
    public void sendSingle(int id, Message message){
        throw new UnsupportedOperationException();
    }

    public Thread getThread() {
        return thread;
    }
}
