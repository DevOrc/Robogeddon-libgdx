package com.noahcharlton.robogeddon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.widget.Label;

public class PauseMenu extends Scene{

    public PauseMenu() {
        var label = new Label().setText("Paused!").setFont(UIAssets.largeFont);
        add(label.pad().top(100)).align(Align.top);
    }

    @Override
    public void update() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            client.resumeGame();
        }
    }
}
