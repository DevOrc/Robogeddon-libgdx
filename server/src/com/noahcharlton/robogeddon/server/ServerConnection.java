package com.noahcharlton.robogeddon.server;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.message.MessageSerializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection {

    private static int idCount;

    final int id = idCount++;
    final Socket socket;
    final DataOutputStream output;
    final DataInputStream input;
    boolean readyForRemoval = false;

    public ServerConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.output = new DataOutputStream(socket.getOutputStream());
        this.input = new DataInputStream(socket.getInputStream());
    }

    public void sendMessage(Message message) {
        try {
            var messageData = MessageSerializer.messageToString(message);

            output.writeUTF(messageData);
            output.flush();
        } catch(IOException e) {
            Log.debug("Failed to send message for client " + id, e);
            close();
        }
    }

    private void close(){
        try {
            socket.close();
        } catch(IOException e) {
            throw new RuntimeException("Failed to close client " + id, e);
        }
    }

    public boolean isReadyForRemoval() {
        return readyForRemoval;
    }
}
