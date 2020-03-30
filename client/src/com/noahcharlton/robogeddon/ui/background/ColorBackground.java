package com.noahcharlton.robogeddon.ui.background;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class ColorBackground implements Background {

    private final Color color = new Color(0x1a1a1aFF);;
    private final Color border = new Color(0x1a1a1aFF);
    private final int borderWidth = 3;

    @Override
    public void draw(SpriteBatch batch, Widget widget) {
        var drawer = Widget.getShapeDrawer();

        drawer.filledRectangle(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), color);
        drawer.rectangle(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight(), border, borderWidth);
    }
}
