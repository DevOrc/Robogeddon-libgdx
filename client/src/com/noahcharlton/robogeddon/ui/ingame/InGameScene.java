package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.selectable.SelectableSubMenu;
import com.noahcharlton.robogeddon.ui.selectable.SelectableSubMenus;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.ui.widget.Widget;
import com.noahcharlton.robogeddon.util.Selectable;

import java.util.Optional;

public class InGameScene extends Scene {

    private final TextButton quitButton = new TextButton("Quit!");
    private final SelectableMenu selectableMenu = new SelectableMenu(this);

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
            closeSelectableSubMenu();
            selectableMenu.setVisible(false);
        }
    }

    private void onQuit(ClickEvent clickEvent, Button button) {
        client.gotoMainMenu();
    }

    public void closeSelectableSubMenu() {
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
