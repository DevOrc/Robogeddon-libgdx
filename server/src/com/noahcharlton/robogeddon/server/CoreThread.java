package com.noahcharlton.robogeddon.server;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.message.MessageSerializer;
import com.noahcharlton.robogeddon.world.ServerWorld;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CoreThread extends ServerProvider {

    @Override
    public void run() {
        Log.info("Starting Core Thread");
        ServerWorld world = new ServerWorld(this);
        Server.runServer(world);
    }

    public void runWithConnection(Socket connection) throws IOException {
        var reader = new DataInputStream(connection.getInputStream());
        var writer = new DataOutputStream(connection.getOutputStream());

        Message m;
        while(!Thread.interrupted()){
            if(reader.available() > 0){
                m = MessageSerializer.parse(reader.readUTF());
                sendMessageToServer(m);
            }

            if((m = getMessageFromServer()) != null){
                String message = MessageSerializer.messageToString(m);
                writer.writeUTF(message);
                writer.flush();
            }
        }
    }


    protected String getName() {
        return "Networking Server";
    }


}
