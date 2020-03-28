package com.noahcharlton.robogeddon;

import com.noahcharlton.robogeddon.world.ServerWorld;

public class Server {

    public static void runServer(ServerWorld world){
        Log.info("Running server with " + world.getServer().getName());
        long frameTime = 1_000_000_000 / Core.UPDATE_RATE;

        long lastFrame = System.nanoTime();
        long nextFPSCheck = System.currentTimeMillis() + 10000;
        int frames = 0;

        while(!Thread.interrupted()){
            while(lastFrame + frameTime <= System.nanoTime()) {
                float diff = System.nanoTime() - lastFrame;
                lastFrame += frameTime;
                frames++;

                if(nextFPSCheck <= System.currentTimeMillis()){
                    Log.debug("Server FPS: " + (frames / 10));
                    frames = 0;
                    nextFPSCheck = System.currentTimeMillis() + 10_000;
                }

                world.update();
            }

            world.updateMessages();
        }
    }
}
