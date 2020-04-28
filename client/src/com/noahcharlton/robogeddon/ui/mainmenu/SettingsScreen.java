package com.noahcharlton.robogeddon.ui.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.settings.Setting;
import com.noahcharlton.robogeddon.settings.SettingsIO;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.PairLayout;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.ui.widget.TextButton;

public class SettingsScreen extends MainMenuSubScreen {

    private final Stack centerPane = new Stack();

    public SettingsScreen() {
        super("Settings");

        buildCenterPane();

        add(centerPane).align(Align.center);
    }

    private void buildCenterPane() {
        centerPane.getChildren().clear();

        for(Setting setting : Core.settings.values()) {
            var layout = new PairLayout()
                    .setLeft(new Label(setting.getName()).setWrap(false).setSize(150, 30))
                    .setRight(new TextButton(setting.getButtonText())
                            .setOnClick((clickEvent, button) -> onButtonClicked(setting))
                            .setSize(150, 30f));

            centerPane.add(layout);
        }

        centerPane.add(new TextButton("Save").setOnClick((clickEvent, button) -> SettingsIO.save())
                        .pad().top(50).left(65).right(65));

        invalidateHierarchy();
    }

    private void onButtonClicked(Setting setting) {
        setting.onButtonClick();
        setting.apply();

        Gdx.app.postRunnable(this::buildCenterPane);
    }

}
