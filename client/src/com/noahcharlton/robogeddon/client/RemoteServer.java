package com.noahcharlton.robogeddon.client;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.message.MessageSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class RemoteServer extends ServerProvider {

    private static final String ip = "localhost";

    @Override
    public void run() {
        Log.info("Starting Remote Server Thread");

        try {
            var socket = waitForConnection();
            runWithConnection(socket);
        } catch(IOException e) {
            throw new RuntimeException("Failed to connect to remote server: " + ip, e);
        }

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
}
