package com.noahcharlton.robogeddon.ui.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class ColorBackground implements Background {

    private final Color color;
    private final Color border;
    private final int borderWidth = 3;

    public ColorBackground() {
        this(new Color(0x2f2f2fFF), new Color(0x2a2a2aFF));
    }

    public ColorBackground(Color color) {
        this(color, color);
    }

    public ColorBackground(Color color, Color border) {
        this.color = color;
        this.border = border;
    }

    @Override
    public void draw(SpriteBatch batch, Widget widget) {
        var drawer = Widget.getShapeDrawer();

        drawer.filledRectangle(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), color);
        drawer.rectangle(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), border, borderWidth);
    }
}
