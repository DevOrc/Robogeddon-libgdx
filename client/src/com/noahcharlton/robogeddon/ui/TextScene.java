package com.noahcharlton.robogeddon.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class TextScene extends Scene {

    protected final Label label = new Label();
    protected final Widget backButton = new TextButton("Back to Main Menu")
            .setOnClick(this::toMainMenu).pad().bottom(100);

    public TextScene() {
        add(label).align(Align.center);
        add(backButton).align(Align.bottom);

        setBackground(new ColorBackground(Color.BLACK));
    }

    private void toMainMenu(ClickEvent clickEvent, Button button) {
        client.gotoMainMenu();
    }

    public TextScene setText(String text) {
        label.setText(text);

        return this;
    }

    public Label getLabel() {
        return label;
    }

    public Widget getBackButton() {
        return backButton;
    }
}
