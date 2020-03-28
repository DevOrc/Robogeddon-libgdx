package com.noahcharlton.robogeddon;

import com.noahcharlton.robogeddon.world.ServerWorld;

import java.util.LinkedList;
import java.util.Queue;

public class Server {

    private static final Queue<Runnable> runLaters = new LinkedList<>();

    public static void runServer(ServerWorld world){
        runServer(world, () -> {});
    }

    public static void runServer(ServerWorld world, Runnable update){
        Log.info("Running server with " + world.getServer().getName());
        long frameTime = 1_000_000_000 / Core.UPDATE_RATE;

        long lastFrame = System.nanoTime();
        long nextFPSCheck = System.currentTimeMillis() + 10000;
        int frames = 0;

        while(!Thread.interrupted()){
            while(lastFrame + frameTime <= System.nanoTime()) {
                lastFrame += frameTime;
                frames++;

                if(nextFPSCheck <= System.currentTimeMillis()){
                    Log.debug("Server FPS: " + (frames / 10));
                    frames = 0;
                    nextFPSCheck = System.currentTimeMillis() + 10_000;
                }

                world.update();
                runRunLaters();
            }

            update.run();
            world.updateMessages();
        }
    }

    private static void runRunLaters() {
        Runnable run;

        while((run = runLaters.poll()) != null){
            run.run();
        }
    }

    public static void runLater(Runnable runnable){
        runLaters.add(runnable);
    }
}
