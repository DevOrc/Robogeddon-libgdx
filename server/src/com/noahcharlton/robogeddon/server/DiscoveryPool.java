package com.noahcharlton.robogeddon.server;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DiscoveryPool implements Runnable{

    private final Queue<Socket> newConnections = new ConcurrentLinkedQueue<>();

    public DiscoveryPool() {
        Thread thread = new Thread(this, "Discovery Pool");
        thread.setDaemon(false);
        thread.start();
    }

    @Override
    public void run() {
        try(var serverSocket = new ServerSocket(Core.PORT)){
            serverSocket.setSoTimeout(0);

            while(!Thread.interrupted()){
                var conn = serverSocket.accept();
                Log.info("New connection: " + conn.getInetAddress());

                newConnections.add(conn);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Queue<Socket> getNewConnections() {
        return newConnections;
    }
}
