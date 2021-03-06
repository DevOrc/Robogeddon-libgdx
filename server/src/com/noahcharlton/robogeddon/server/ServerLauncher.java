package com.noahcharlton.robogeddon.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.message.MessageSerializer;
import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.world.LostClientMessage;

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
        removeClosedConnections();
    }

    private void removeClosedConnections() {
        for(ServerConnection connection : connections) {
            if(connection.socket.isClosed()){
                Log.info("Lost Client " + connection.id);
                gameThread.sendMessageToServer(new LostClientMessage(connection.id));
                connection.readyForRemoval = true;
            }
        }

        connections.removeIf(ServerConnection::isReadyForRemoval);
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
        Log.init();
        Log.info("Server Launcher started!");
        Log.info("Core Version: " + Core.VERSION + "-" + Core.VERSION_TYPE);

        Core.preInit();
        loadAssets();

        Log.info("Running server!");
        new ServerLauncher();
    }

    private static void loadAssets() {
        Log.info("Loading Assets: " + Core.assets.getAssetCount());
        Gdx.files = new HeadlessFiles();

        while(!Core.assets.isDone()){
            Core.assets.update();
        }

        Log.info("Finished Loading Assets!");
    }
}
