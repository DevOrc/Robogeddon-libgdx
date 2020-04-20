package com.noahcharlton.robogeddon.client;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.message.MessageSerializer;
import com.noahcharlton.robogeddon.world.settings.RemoteWorldSettings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class RemoteServer extends ServerProvider {

    private final String ip;

    public RemoteServer(RemoteWorldSettings settings) {
        ip = settings.getIp();
    }

    @Override
    public void run() {
        Log.info("Starting Remote Server Thread");

        try {
            var socket = waitForConnection();

            if(socket != null) //socket is null if the user quit the game, while connecting
                runWithConnection(socket);
        } catch(IOException e) {
            throw new RuntimeException("Failed to connect to remote server: " + ip, e);
        }

        Log.info("Shutting down remote server connection!");
    }

    public void runWithConnection(Socket connection) throws IOException {
        var reader = new DataInputStream(connection.getInputStream());
        var writer = new DataOutputStream(connection.getOutputStream());

        Message m;
        while(!Thread.interrupted()){
            if(reader.available() > 0){
                m = MessageSerializer.parse(reader.readUTF());
                sendMessageToClient(m);
            }

            if((m = getMessageFromClient()) != null){
                String message = MessageSerializer.messageToString(m);
                writer.writeUTF(message);
                writer.flush();
            }
        }
    }


    private Socket waitForConnection() throws IOException {
        while(!Thread.interrupted()){
            try{
                var conn = new Socket(ip, Core.PORT);
                if(conn.isConnected()){
                    Log.info("Found connection at: " + conn.getInetAddress());
                    return conn;
                }
            }catch(SocketTimeoutException | ConnectException e){}
        }

        return null;
    }

    @Override
    public String getName() {
        return "Remote Server";
    }

    @Override
    public boolean isRemote() {
        return true;
    }
}
