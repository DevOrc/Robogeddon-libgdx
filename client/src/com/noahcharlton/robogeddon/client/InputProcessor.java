package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.Gdx;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.ui.UI;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;

public class InputProcessor implements com.badlogic.gdx.InputProcessor {

    private final UI ui;

    public InputProcessor(UI ui) {
        this.ui = ui;

        Log.debug("Initialized Input Processor");
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY;

        if(ui.isMouseOver()){
            ui.onClick(new ClickEvent(ui, screenX, screenY, button));
        }

        return false;
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
    public boolean touchDragged(int screenX, int screenY, int pointer) {
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
