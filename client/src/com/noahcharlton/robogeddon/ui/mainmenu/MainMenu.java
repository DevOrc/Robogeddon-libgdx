package com.noahcharlton.robogeddon.ui.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.world.settings.NewWorldSettings;
import com.noahcharlton.robogeddon.world.settings.RemoteWorldSettings;

public class MainMenu extends Scene {

    public MainMenu() {
        var versionText =  "v" + Core.VERSION + "-" + Core.VERSION_TYPE;
        var title = new Label().setText("Robogeddon").setFont(UIAssets.titleFont).pad().top(100);
        var version = new Label().setText(versionText).setFont(UIAssets.smallFont)
                .pad().right(20).bottom(5);

        var buttonStack = new Stack()
                .chainAdd(new TextButton("Singleplayer").setOnClick(this::playSingle).setSize(145f, 30f))
                .chainAdd(new TextButton("Load Game").setOnClick(this::loadGame).setSize(145f, 30f))
                .chainAdd(new TextButton("Multiplayer").setOnClick(this::playMulti).setSize(145f, 30f))
                .chainAdd(new TextButton("Quit").setOnClick(this::quit).setSize(145f, 30f))
                .pad().bottom(100);

        add(title).align(Align.top);
        add(version).align(Align.bottomRight);
        add(buttonStack).align(Align.bottom);
    }

    private void loadGame(ClickEvent clickEvent, Button button) {
        clickEvent.getUi().setScene(new LoadGameScreen());
    }

    private void quit(ClickEvent clickEvent, Button button) {
        Gdx.app.exit();
    }

    private void playMulti(ClickEvent clickEvent, Button button) {
        client.startGame(new RemoteWorldSettings("localhost"));
    }

    private void playSingle(ClickEvent clickEvent, Button button) {
        client.startGame(new NewWorldSettings());
    }

    @Override
    public boolean isMouseOver() {
        //the main menu covers the whole screen, so the mouse is always over it
        return true;
    }
}
