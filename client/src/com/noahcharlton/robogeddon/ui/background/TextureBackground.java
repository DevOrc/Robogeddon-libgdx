package com.noahcharlton.robogeddon.ui.background;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class TextureBackground implements Background{

    private final TextureRegion texture;

    public TextureBackground(TextureRegion texture) {
        this.texture = texture;
    }

    @Override
    public void draw(SpriteBatch batch, Widget widget) {
        batch.draw(texture, widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
    }
}
