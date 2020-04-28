package com.noahcharlton.robogeddon.ui.mainmenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class MainMenuSubScreen extends Scene {

    protected Widget titleLabel;
    protected Widget backButton;

    public MainMenuSubScreen(String title) {
        titleLabel = new Label().setText(title).setFont(UIAssets.largeFont).pad().top(50);
        backButton = new TextButton("Back").setOnClick(this::toMainMenu).pad().bottom(100);

        add(titleLabel).align(Align.top);
        add(backButton).align(Align.bottom);
        setBackground(new ColorBackground(Color.BLACK));
    }

    private void toMainMenu(ClickEvent clickEvent, Button button) {
        client.gotoMainMenu();
    }
}
