package com.noahcharlton.robogeddon.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.client.GameClient;
import com.noahcharlton.robogeddon.ui.UI;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.world.Tile;

public class InputProcessor implements com.badlogic.gdx.InputProcessor {

    private final GameClient client;
    private final UI ui;

    private BuildAction buildAction;
    private Tile lastTile;

    public InputProcessor(GameClient client) {
        this.client = client;
        this.ui = client.getUi();

        Log.debug("Initialized Input Processor");
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY;

        if(ui.isMouseOver()){
            ui.onClick(new ClickEvent(ui, screenX, screenY, button));
        }else if(buildAction != null){
            Vector3 pos = Core.client.mouseToWorld();
            Tile tile = client.getWorld().tileFromPixel(pos);

            if(tile != null){
                buildAction.onClick(tile, button);
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(ui.isMouseOver() || buildAction == null){
            return false;
        }

        Vector3 pos = Core.client.mouseToWorld();
        Tile tile = client.getWorld().tileFromPixel(pos);

        if(tile != null && !tile.equals(lastTile)){
            if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                buildAction.onClick(tile, Input.Buttons.LEFT);
            }else if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
                buildAction.onClick(tile, Input.Buttons.RIGHT);
            }
        }

        lastTile = tile;

        return false;
    }

    public void setBuildAction(BuildAction buildAction) {
        if(buildAction != null){
            Log.info("Set Build Action: " + buildAction.getName());
        }else{
            Log.info("Set Build Action: None");
        }

        this.buildAction = buildAction;
    }

    public BuildAction getBuildAction() {
        return buildAction;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}