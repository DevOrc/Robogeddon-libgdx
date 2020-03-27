package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.ServerProvider;

public class ServerWorld extends World{

    private final ServerProvider server;

    public ServerWorld(ServerProvider server) {
        this.server = server;
    }

    public void update(){

    }

    public ServerProvider getServer() {
        return server;
    }
}
