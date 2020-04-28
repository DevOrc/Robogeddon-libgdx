package com.noahcharlton.robogeddon.ui.mainmenu;

import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.widget.Label;

public class TextScene extends MainMenuSubScreen {

    protected final Label label = new Label();

    public TextScene(String text) {
        super("");

        label.setText(text);
        add(label).align(Align.center);
    }

    public TextScene setText(String text) {
        label.setText(text);

        return this;
    }

    public Label getLabel() {
        return label;
    }
}
