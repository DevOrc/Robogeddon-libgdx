package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.noahcharlton.robogeddon.Client;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.graphics.GameRenderer;
import com.noahcharlton.robogeddon.input.InputProcessor;
import com.noahcharlton.robogeddon.message.PauseGameMessage;
import com.noahcharlton.robogeddon.settings.Setting;
import com.noahcharlton.robogeddon.ui.mainmenu.TextScene;
import com.noahcharlton.robogeddon.ui.mainmenu.WorldLoadingScene;
import com.noahcharlton.robogeddon.ui.mainmenu.MainMenu;
import com.noahcharlton.robogeddon.ui.pause.PauseMenu;
import com.noahcharlton.robogeddon.ui.UI;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.ingame.InGameScene;
import com.noahcharlton.robogeddon.ui.selectable.SelectableSubMenus;
import com.noahcharlton.robogeddon.world.ClientWorld;
import com.noahcharlton.robogeddon.world.MainMenuWorld;
import com.noahcharlton.robogeddon.world.settings.WorldSettings;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GameClient extends ApplicationAdapter implements Client {

    private static GameClient instance = new GameClient();

    private boolean loadingAssets = true;
    private long updateFrameTime = 1_000_000_000 / Core.UPDATE_RATE;
    private long updateLastFrame;
    private long nextFpsCheck;
    private int updateFrames;
    private int renderFrames;

    private InputProcessor processor;
    private GameRenderer renderer;
    private ClientWorld world;
    private UI ui;

    @Override
    public void create() {
        Thread.currentThread().setName("Client");
        Log.info("Game Client created!");
        Core.init();
        UIAssets.init();
        SelectableSubMenus.init();

        renderer = new GameRenderer(this);
        ui = new UI(this);
        processor = new InputProcessor(this);
        setWorld(new MainMenuWorld());
        Setting.applyAll();

        Log.info("Assets registered: " + Core.assets.getAssetCount());
    }

    @Override
    public void render() {
        if(loadingAssets){
            updateAssetLoading();
            ui.renderLoadingScreen();
            return;
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            loadingAssets = true;
            Core.assets.reload();
            return;
        }else if(!world.getServer().isConnected() && ui.getCurrentScene() instanceof InGameScene){
            onServerDisconnect();
        }

        while(updateLastFrame + updateFrameTime <= System.nanoTime()){
            updateLastFrame += updateFrameTime;
            updateFrames++;

            if(ui.getCurrentScene().isWorldVisible()){
                world.update();
                processor.update();
            }
        }

        if(ui.getCurrentScene().isWorldVisible()) {
            renderer.render();
        }
        renderFrames++;
        ui.render();

        world.updateMessages();
        updateFPSCount();
    }

    public void onServerDisconnect() {
        if(world.getServer() instanceof LocalServer){
            ui.setScene(new TextScene("Local server crash! See log for more details"));
        }else if(world.getServer() instanceof RemoteServer){
            ui.setScene(new TextScene("Remote server disconnect! See log for more details"));
        }else{
            ui.setScene(new TextScene("Server Disconnected for unknown reasons!"));
        }
    }

    public void startGame(WorldSettings settings){
        Log.info("Starting new world: " + settings);

        updateLastFrame = System.nanoTime();
        nextFpsCheck = System.currentTimeMillis() + 10000;
        setWorld(new ClientWorld(settings));
        ui.setScene(new WorldLoadingScene());

        processor.setSelectable(null);
        processor.setBuildAction(null);
    }

    public void pauseGame() {
        if(world == null)
            throw new UnsupportedOperationException("Cannot pause when not in game!");

        Log.info("Pausing game!");
        ui.setScene(new PauseMenu());
        world.sendMessageToServer(new PauseGameMessage(true));
    }

    @Override
    @Deprecated
    public void pause() {}

    public void resumeGame() {
        if(world == null)
            throw new UnsupportedOperationException("Cannot resume when not in game!");

        Log.info("Resuming game!");
        ui.setScene(new InGameScene());
        world.sendMessageToServer(new PauseGameMessage(false));
    }

    @Override
    @Deprecated
    public void resume(){}

    public void gotoMainMenu(){
        Log.info("Going to main menu");
        ui.setScene(new MainMenu());
        setWorld(new MainMenuWorld());
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
        ui.setScene(new MainMenu());

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

    private void setWorld(ClientWorld world){
        if(this.world != null)
            this.world.shutdown();

        this.world = world;
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

    @Override
    public boolean isMouseOnUI() {
        return ui.isMouseOver();
    }

    @Override
    public boolean isPauseMenuOpen() {
        return ui.getCurrentScene() instanceof PauseMenu;
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

    public InputProcessor getProcessor() {
        return processor;
    }

    public UI getUi() {
        return ui;
    }

    public GameRenderer getRenderer() {
        return renderer;
    }
}
