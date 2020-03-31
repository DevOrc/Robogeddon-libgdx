package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.input.BuildBlock;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.TextButton;

public class InGameScene extends Scene {

    private final TextButton button = new TextButton("Hello, please click me!");
    private int alignIndex = 0;

    public InGameScene() {
        add(button).setOnClick(this::onClick).align(Align.bottomLeft);
        add(new InventoryList()).align(Align.topLeft);
    }

    private void onClick(ClickEvent clickEvent, Button button) {
        client.getProcessor().setBuildAction(new BuildBlock(Blocks.testBlock));
    }
}
