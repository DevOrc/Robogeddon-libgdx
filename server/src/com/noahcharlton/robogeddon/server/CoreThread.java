package com.noahcharlton.robogeddon.server;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.ServerProvider;
import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.settings.NewWorldSettings;

import java.util.AbstractMap.SimpleEntry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CoreThread extends ServerProvider {

    private final Queue<Integer> newConnections = new ConcurrentLinkedQueue<>();
    private final Queue<SimpleEntry<Integer, Message>> singleMessages = new ConcurrentLinkedQueue<>();

    private ServerWorld world;

    @Override
    public void run() {
        Log.info("Starting Core Thread");
        world = new ServerWorld(this, new NewWorldSettings());
        Server.runServer(world, this::update);
    }

    private void update() {
        Integer id;

        while((id = newConnections.poll()) != null){
            world.handleNewConnection(id);
        }
    }

    public void addNewConnection(int conn){
        newConnections.add(conn);
    }

    protected String getName() {
        return "Networking Server";
    }

    @Override
    public void sendSingle(int id, Message message){
        singleMessages.add(new SimpleEntry<>(id, message));
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    SimpleEntry<Integer, Message> getNextSingleMessage() {
        return singleMessages.poll();
    }

    @Override
    public boolean isRemote() {
        return true;
    }
}
