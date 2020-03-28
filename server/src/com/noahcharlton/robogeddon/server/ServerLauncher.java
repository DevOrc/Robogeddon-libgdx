package com.noahcharlton.robogeddon.server;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ServerLauncher {

    public ServerLauncher() {
        try{
            run();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void run() throws IOException{
        Log.info("Waiting for connection!");
        Socket connection = waitForConnection();
        Log.info("New Connection: " + connection.getInetAddress());
        new CoreThread().runWithConnection(connection);
    }

    private Socket waitForConnection() throws IOException {
        var serverSocket = new ServerSocket(Core.PORT);

        while(!Thread.interrupted()){
            try{
                return serverSocket.accept();
            }catch(SocketTimeoutException e){
            }
        }

        throw new RuntimeException();
    }

    public static void main(String[] args) {
        Log.info("Server Launcher started!");
        Log.info("Core Version: " + Core.VERSION);

        Core.preInit();
        new ServerLauncher();
    }
}
