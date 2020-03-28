package com.noahcharlton.robogeddon.client;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.world.ServerWorld;

public class LocalServer extends ServerProvider {

    private ServerWorld world;

    @Override
    public void run() {
        Log.info("Starting local server!");
        world = new ServerWorld(this);

        Server.runServer(world);
    }

    @Override
    public String getName() {
        return "Local Server Thread";
    }
}
