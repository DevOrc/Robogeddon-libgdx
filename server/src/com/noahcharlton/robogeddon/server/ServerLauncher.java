package com.noahcharlton.robogeddon.server;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.message.MessageSerializer;

import java.io.IOException;
import java.net.Socket;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

public class ServerLauncher {

    private final List<ServerConnection> connections = new ArrayList<>();

    private final DiscoveryPool discoveryPool;
    private final CoreThread gameThread;

    public ServerLauncher() {
        discoveryPool = new DiscoveryPool();
        gameThread = new CoreThread();

        while(!Thread.interrupted()){
            try {
                update();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() throws IOException {
        handleNewConnections();
        handleReceivedMessages();
        sendOutgoingMessages();
        sendSingleMessages();
    }

    private void sendSingleMessages() {
        SimpleEntry<Integer, Message> message;

        while((message = gameThread.getNextSingleMessage()) != null){
            ServerConnection conn = getFromID(message.getKey());

            if(conn == null){
                Log.warn("Invalid conn ID: " + message.getKey());
                continue;
            }

            conn.sendMessage(message.getValue());
        }
    }

    private ServerConnection getFromID(int id){
        for(ServerConnection connection : connections) {
            if(id == connection.id)
                return connection;
        }

        return null;
    }

    private void sendOutgoingMessages()  {
        Message m;

        while((m = gameThread.getMessageFromServer()) != null){
            sendMessage(m);
        }
    }

    private void sendMessage(Message message)  {
        for(ServerConnection connection : connections) {
            connection.sendMessage(message);
        }
    }

    private void handleReceivedMessages() throws IOException {
        for(ServerConnection connection : connections) {
            if(connection.input.available() > 0){
                var message = MessageSerializer.parse(connection.input.readUTF());
                gameThread.sendMessageToServer(message);
            }
        }
    }

    private void handleNewConnections() throws IOException {
        Socket conn;
        while((conn = discoveryPool.getNewConnections().poll()) != null){
            var connection = new ServerConnection(conn);
            connections.add(connection);
            gameThread.addNewConnection(connection.id);
            Log.debug("New connection ID: " + connection.id);
        }
    }

    public static void main(String[] args) {
        Log.info("Server Launcher started!");
        Log.info("Core Version: " + Core.VERSION);

        Core.preInit();

        Log.info("Running server!");
        new ServerLauncher();
    }
}
