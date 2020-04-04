package com.noahcharlton.robogeddon.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.ui.widget.TextButton;

public class MainMenu extends Scene{

    public MainMenu() {
        var title = new Label().setText("Robogeddon").setFont(UIAssets.titleFont).pad().top(100);
        var version = new Label().setText("Version: " + Core.VERSION).setFont(UIAssets.smallFont)
                .pad().right(20).bottom(5);

        var buttonStack = new Stack()
                .chainAdd(new TextButton("Singleplayer").setOnClick(this::playSingle).setSize(145f, 30f))
                .chainAdd(new TextButton("Multiplayer").setOnClick(this::playMulti).setSize(145f, 30f))
                .chainAdd(new TextButton("Quit").setOnClick(this::quit).setSize(145f, 30f))
                .pad().bottom(100);

        add(title).align(Align.top);
        add(version).align(Align.bottomRight);
        add(buttonStack).align(Align.bottom);
    }

    private void quit(ClickEvent clickEvent, Button button) {
        Gdx.app.exit();
    }

    private void playMulti(ClickEvent clickEvent, Button button) {
        client.startGame(false);
    }

    private void playSingle(ClickEvent clickEvent, Button button) {
        client.startGame(true);
    }

    @Override
    public boolean isMouseOver() {
        //The mouse is always over the main menu
        return true;
    }
}
