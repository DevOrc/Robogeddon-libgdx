package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.client.ServerThread;

public class ClientWorld extends World {

    private final ServerThread server;

    public ClientWorld() {
        this.server = new ServerThread();
    }

    public void fixedUpdate(){

    }

    public void updateMessages(){

    }

    public ServerThread getServer() {
        return server;
    }
}
