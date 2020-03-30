package com.noahcharlton.robogeddon.ui.event;

import com.noahcharlton.robogeddon.ui.UI;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class ClickEvent {

    private final UI ui;
    private final int x;
    private final int y;
    private final int button;

    public ClickEvent(UI ui, int x, int y, int button) {
        this.ui = ui;
        this.x = x;
        this.y = y;
        this.button = button;
    }

    public boolean isOnWidget(Widget widget){
        return x > widget.getX() && y > widget.getY() && x < widget.getX() + widget.getWidth()
                && y < widget.getY() + widget.getHeight();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getButton() {
        return button;
    }

    public UI getUi() {
        return ui;
    }
}
