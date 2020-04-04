package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.input.BuildBlock;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.TextButton;

public class InGameScene extends Scene {

    private final TextButton buildButton = new TextButton("Build!");
    private final TextButton quitButton = new TextButton("Quit!");

    public InGameScene() {
        add(buildButton).setOnClick(this::onStartBuild).align(Align.bottomLeft);
        add(quitButton).setOnClick(this::onQuit).align(Align.bottomRight);
        add(new InventoryList()).align(Align.topLeft);
    }

    private void onQuit(ClickEvent clickEvent, Button button) {
        client.gotoMainMenu();
    }

    private void onStartBuild(ClickEvent clickEvent, Button button) {
        client.getProcessor().setBuildAction(new BuildBlock(Blocks.testBlock));
    }
}
