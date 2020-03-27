package com.noahcharlton.robogeddon;

import com.noahcharlton.robogeddon.world.ServerWorld;

public class Core {

    public static final String VERSION = "0.1.0";

    public static void runServer(ServerWorld world){
        Log.info("Running server with " + world.getServer().getName());
        long frameTime = 1_000_000_000 / 60;

        long nextFrame = System.nanoTime() + frameTime;
        long nextFPSCheck = System.currentTimeMillis() + 10000;
        int frames = 0;

        while(!Thread.interrupted()){
            while(nextFrame <= System.nanoTime()) {
                nextFrame += frameTime;
                frames++;

                if(nextFPSCheck <= System.currentTimeMillis()){
                    Log.trace("Server FPS: " + (frames / 10));
                    frames = 0;
                    nextFPSCheck = System.currentTimeMillis() + 10_000;
                }

                fixedUpdate(world);
            }

            update(world);
        }
    }

    private static void update(ServerWorld world) {

    }

    private static void fixedUpdate(ServerWorld world) {

    }

}
