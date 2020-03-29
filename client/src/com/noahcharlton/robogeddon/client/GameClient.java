package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.noahcharlton.robogeddon.Client;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.graphics.GameRenderer;
import com.noahcharlton.robogeddon.ui.UI;
import com.noahcharlton.robogeddon.world.ClientWorld;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GameClient extends ApplicationAdapter implements Client {

    private static GameClient instance = new GameClient();

    private boolean loadingAssets = true;
    private long updateFrameTime = 1_000_000_000 / Core.UPDATE_RATE;
    private long updateLastFrame;
    private long nextFpsCheck;
    private int updateFrames;
    private int renderFrames;

    private GameRenderer renderer;
    private ClientWorld world;
    private UI ui;

    @Override
    public void create() {
        Thread.currentThread().setName("Client");
        Log.info("Game Client created!");
        Core.init();

        renderer = new GameRenderer(this);
        ui = new UI(this);
        world = new ClientWorld();
    }

    @Override
    public void render() {
        if(loadingAssets){
            updateAssetLoading();
            return;
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            loadingAssets = true;
            Core.assets.reload();
            return;
        }

        while(updateLastFrame + updateFrameTime <= System.nanoTime()){
            updateLastFrame += updateFrameTime;
            updateFrames++;
            world.update();
        }

        renderFrames++;
        renderer.render();
        ui.render(world);

        world.updateMessages();
        updateFPSCount();
    }

    private void updateAssetLoading() {
        Core.assets.update();

        if(Core.assets.isDone()){
            onAssetsLoaded();
        }
    }

    private void onAssetsLoaded(){
        loadingAssets = false;
        updateLastFrame = System.nanoTime();
        nextFpsCheck = System.currentTimeMillis() + 10000;

        Log.info("Finished loading assets!");
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

    @Override
    public Vector3 mouseToWorld() {
        var mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        return renderer.getCamera().unproject(mouse);
    }

    @Override
    public void resize(int width, int height) {
        Log.info("Resize: (" + width + ", " + height + ")");
        renderer.resize(width, height);
        ui.resize(width, height);
    }

    @Override
    public void dispose() {
        Core.assets.dispose();
        Log.info("Game Client disposed!");
    }

    public static GameClient getInstance() {
        return instance;
    }

    public ClientWorld getWorld() {
        return world;
    }

    @Override
    public ShapeDrawer getGameShapeDrawer() {
        return renderer.getGameShapeDrawer();
    }
}
