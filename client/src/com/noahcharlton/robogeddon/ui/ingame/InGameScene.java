package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.TextButton;

public class InGameScene extends Scene {

    private final TextButton quitButton = new TextButton("Quit!");
    private final SelectableMenu selectableMenu = new SelectableMenu();

    public InGameScene() {
        add(quitButton).setOnClick(this::onQuit).align(Align.bottom);
        add(selectableMenu).align(Align.bottomRight);
        add(new InventoryList()).align(Align.topLeft);
        add(new BuildMenu()).align(Align.bottomLeft).pad().bottom(1).left(1);
    }

    @Override
    public void update() {
        if(client.getProcessor().getSelectable() != null){
            selectableMenu.setVisible(true);
        }else{
            selectableMenu.setVisible(false);
        }
    }

    private void onQuit(ClickEvent clickEvent, Button button) {
        client.gotoMainMenu();
    }
}
