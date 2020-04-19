package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.selectable.SelectableSubMenu;
import com.noahcharlton.robogeddon.ui.selectable.SelectableSubMenus;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.ui.widget.Widget;
import com.noahcharlton.robogeddon.util.Selectable;
import com.noahcharlton.robogeddon.world.io.SaveWorldMessage;

import java.util.Optional;

public class InGameScene extends Scene {

    private final TextButton quitButton = new TextButton("Quit!");
    private final TextButton saveButton = new TextButton("Save!");
    private final SelectableMenu selectableMenu = new SelectableMenu(this);

    public InGameScene() {
        add(saveButton).setOnClick(this::onSave).align(Align.topRight);
        add(quitButton).setOnClick(this::onQuit).align(Align.bottom);
        add(selectableMenu).align(Align.bottomRight);
        add(new InventoryList()).align(Align.topLeft);
        add(new BuildMenu()).align(Align.bottomLeft).pad().bottom(1).left(1);
    }

    private void onSave(ClickEvent clickEvent, Button button) {
        if(!client.getWorld().getServer().isRemote()) {
            client.getWorld().sendMessageToServer(new SaveWorldMessage());
        }else{
            Log.warn("Can't save remote server??");
        }
    }

    @Override
    public void update() {
        if(client.getProcessor().getSelectable() != null){
            selectableMenu.setVisible(true);
        }else{
            closeSelectableSubMenu();
            selectableMenu.setVisible(false);
        }

        if(client.getWorld() != null)
            saveButton.setVisible(!client.getWorld().getServer().isRemote());

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            client.pauseGame();
        }
    }

    private void onQuit(ClickEvent clickEvent, Button button) {
        client.gotoMainMenu();
    }

    public void closeSelectableSubMenu() {
        boolean close = false;
        var selectable = client.getProcessor().getSelectable();

        if(selectable == null || getSelectableSubMenu().isEmpty()){
            close = true;
        }else { //If the current selectable matches the menu, it was opened automatically (via right click),
            //so it should not be closed
            var selectableSubMenu = (SelectableSubMenu) getSelectableSubMenu().get();

            close = !selectableSubMenu.getId().equals(selectable.getSubMenuID());
        }

        if(close)
            getSelectableSubMenu().ifPresent(widget -> getChildren().remove(widget));
    }

    public void toggleSelectableSubMenu(Selectable selectable) {
        var subMenu = getSelectableSubMenu();

        if(subMenu.isPresent()){
            getChildren().remove(subMenu.get());
        }else{
            add(SelectableSubMenus.createFrom(selectable.getSubMenuID())).align(Align.center);
        }
    }

    public Optional<Widget> getSelectableSubMenu() {
        return getChildren().stream().filter(widget -> widget instanceof SelectableSubMenu).findAny();
    }
}
