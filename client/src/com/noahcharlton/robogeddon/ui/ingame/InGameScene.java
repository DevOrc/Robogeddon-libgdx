package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.TextButton;

public class InGameScene extends Scene {

    private final TextButton quitButton = new TextButton("Quit!");

    public InGameScene() {
        add(quitButton).setOnClick(this::onQuit).align(Align.bottomRight);
        add(new InventoryList()).align(Align.topLeft);
        add(new BuildMenu()).align(Align.bottomLeft).pad().bottom(1).left(1);
    }

    private void onQuit(ClickEvent clickEvent, Button button) {
        client.gotoMainMenu();
    }
}
