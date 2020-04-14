package com.noahcharlton.robogeddon.client;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.world.ServerWorld;

public class LocalServer extends ServerProvider {

    private ServerWorld world;
    private boolean worldLoaded = false;
    private boolean loadWorld;

    public LocalServer(boolean load) {
        this.loadWorld = load;
    }

    @Override
    public void run() {
        Log.info("Starting local server!");
        world = new ServerWorld(this, loadWorld);
        worldLoaded = true;
        Log.info("World loaded, connecting client");
        world.handleNewConnection(0);

        Server.runServer(world);

        Log.info("Shutting down local server!");
    }

    @Override
    public void sendMessageToServer(Message message) {
        if(worldLoaded)
            super.sendMessageToServer(message);
    }

    @Override
    public void sendMessageToClient(Message message) {
        if(worldLoaded)
            super.sendMessageToClient(message);
    }

    @Override
    public void sendSingle(int id, Message message) {
        //Since we are local, send the the message to the client
        //because they are the only player
        Log.trace("Sending message to client of type" + message.getClass());
        sendMessageToClient(message);
    }

    @Override
    public String getName() {
        return "Local Server Thread";
    }

    @Override
    public boolean isRemote() {
        return false;
    }
}
