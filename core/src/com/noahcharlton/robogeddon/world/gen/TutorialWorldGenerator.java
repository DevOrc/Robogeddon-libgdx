package com.noahcharlton.robogeddon.world.gen;

import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.message.ChatMessage;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.ServerWorld;

public class TutorialWorldGenerator extends WorldGenerator {

    public TutorialWorldGenerator() {
        super(Long.parseLong("-6218232092291161956"));
    }

    @Override
    public void createInitialWorld(ServerWorld world) {
        for(int x = -1; x <= 0; x++) {
            for(int y = -1; y <= 0; y++) {
                var chunk = world.createChunk(x, y, true);
                chunk.setTeam(world.getPlayerTeam());
            }
        }

        //Send it after the world is created (so that the player is connected)
        Server.runLater(() -> sendStartChat(world));
    }

    private void sendStartChat(ServerWorld world) {
        world.sendMessageToClient(new ChatMessage("Welcome to the tutorial:"));
        world.sendMessageToClient(new ChatMessage(""));
        world.sendMessageToClient(new ChatMessage("You can fly around by pressing WASD"));
        world.sendMessageToClient(new ChatMessage("and zoom in and out by scrolling"));
    }

    @Override
    public void genChunksAround(ServerWorld world, Chunk chunk) {
        //No new chunks are generated in the tutorial
    }
}
