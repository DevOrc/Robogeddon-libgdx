package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.graphics.GameRenderer;
import com.noahcharlton.robogeddon.world.ClientWorld;

public class GameClient extends ApplicationAdapter {

    private static GameClient instance = new GameClient();

    private long updateFrameTime = 1_000_000_000 / 60;
    private long updateNextFrameTime;
    private long nextFpsCheck;
    private int updateFrames;
    private int renderFrames;

    private GameRenderer renderer;
    private ClientWorld world;

    @Override
    public void create() {
        Thread.currentThread().setName("Client");
        Log.info("Game Client created!");
        Core.init();

        renderer = new GameRenderer(this);
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
        renderer.render();

        update();
        updateFPSCount();
    }

    private void updateFPSCount() {
        if(nextFpsCheck <= System.currentTimeMillis()){
            Log.debug("Client Update FPS: " + (updateFrames / 10));
            Log.debug("Client Render FPS: " + (renderFrames / 10));

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
        Log.info("Resize: (" + width + ", " + height + ")");
        renderer.resize(width, height);
    }

    @Override
    public void dispose() {
        Log.info("Game Client disposed!");
    }

    public static GameClient getInstance() {
        return instance;
    }

    public ClientWorld getWorld() {
        return world;
    }
}
