package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.world.ClientWorld;

public class GameClient extends ApplicationAdapter {

    private static GameClient instance = new GameClient();

    private long updateFrameTime = 1_000_000_000 / 60;
    private long updateNextFrameTime;
    private long nextFpsCheck;
    private int updateFrames;
    private int renderFrames;

    private ClientWorld world;

    @Override
    public void create() {
        Thread.currentThread().setName("Client");
        Log.info("Game Client created!");

        world = new ClientWorld();

        updateNextFrameTime = System.nanoTime() + updateNextFrameTime;
        nextFpsCheck = System.currentTimeMillis() + 10000;
    }

    @Override
    public void render() {
        while(updateNextFrameTime <= System.nanoTime()){
            updateNextFrameTime += updateFrameTime;
            updateFrames++;
            fixedUpdate();
        }

        renderFrames++;

        update();
        updateFPSCount();
    }

    private void updateFPSCount() {
        if(nextFpsCheck <= System.currentTimeMillis()){
            Log.trace("Client Update FPS: " + (updateFrames / 10));
            Log.trace("Client Render FPS: " + (renderFrames / 10));

            nextFpsCheck += 10000;
            renderFrames = 0;
            updateFrames = 0;
        }
    }

    private void update() {
        world.updateMessages();
    }

    private void fixedUpdate() {
        world.fixedUpdate();
    }

    @Override
    public void resize(int width, int height) {
        Log.debug("Resize: (" + width + ", " + height + ")");
    }

    @Override
    public void dispose() {
        Log.info("Game Client disposed!");
    }

    public static GameClient getInstance() {
        return instance;
    }
}
