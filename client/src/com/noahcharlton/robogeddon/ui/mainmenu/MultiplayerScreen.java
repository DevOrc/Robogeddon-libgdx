package com.noahcharlton.robogeddon.ui.mainmenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.*;
import com.noahcharlton.robogeddon.world.settings.RemoteWorldSettings;

public class MultiplayerScreen extends Scene {

    public MultiplayerScreen() {
        var title = new Label().setText("Multiplayer").setFont(UIAssets.largeFont).pad().top(50);
        var backButton = new TextButton("Back").setOnClick(this::toMainMenu).pad().bottom(100);
        var ipField = new TextField().setPromptText("Server IP").setText("localhost");
        var centerWidget = new PairLayout()
                .setLeft(ipField.setWidth(250))
                .setRight(new TextButton("Join").setOnClick((clickEvent, button) -> connect(ipField)))
                .setSpacing(10);

        add(title).align(Align.top);
        add(backButton).align(Align.bottom);
        add(centerWidget).align(Align.center);
        setBackground(new ColorBackground(Color.BLACK));
    }

    private void connect(TextField ipField) {
        client.startGame(new RemoteWorldSettings(ipField.getText()));
    }

    private void toMainMenu(ClickEvent clickEvent, Button button) {
        client.gotoMainMenu();
    }
}
