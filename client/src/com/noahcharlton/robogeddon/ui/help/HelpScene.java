package com.noahcharlton.robogeddon.ui.help;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.ui.Scene;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.util.help.HelpInfo;

public abstract class HelpScene extends Scene {

    protected final HelpInfo info;

    public HelpScene(HelpInfo info) {
        this.info = info;

        add(new TextButton("Back to Game").setOnClick(this::resume).pad().bottom(60)).align(Align.bottom);
        setBackground(new ColorBackground(Color.BLACK));
    }

    private void resume(ClickEvent clickEvent, Button button) {
        client.resumeGame();
    }

    @Override
    public boolean isMouseOver() {
        return true;
    }

    @Override
    public boolean isWorldVisible() {
        return false;
    }
}
