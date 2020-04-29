package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;

public class Link extends Label {

    private static final Color textColor = new Color(0x66B2FFFF);
    private static final Color clickedColor = new Color(0x3399FFFF);

    private String link;

    private float textHeight;
    private float textWidth;

    public Link() {
        setTextColor(textColor);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);

        if(isMouseOver()){
            var y = getY() + getHeight() - textHeight - 3;
            var x = getX() + (getWidth() / 2f) - (textWidth / 2f);

            getShapeDrawer().setColor(getRenderColor());
            getShapeDrawer().filledRectangle(x, y, textWidth, 1.5f);
        }
    }

    @Override
    public void layout() {
        super.layout();

        var layout = getTextLayout();

        textHeight = layout.height;
        textWidth = layout.width;
    }

    @Override
    protected void onClick(ClickEvent event) {
        if(link != null){
            setTextColor(clickedColor);
            setHoverColor(null);
            Gdx.net.openURI(link);
        }
    }

    public Link setLink(String link) {
        this.link = link;
        return this;
    }

    @Override
    public Label setText(String text) {
        return super.setText(text);
    }
}
