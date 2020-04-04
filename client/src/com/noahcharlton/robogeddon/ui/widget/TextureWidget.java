package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureWidget extends NoChildrenWidget{

    private TextureRegion texture;

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public TextureWidget setTexture(TextureRegion texture) {
        this.texture = texture;
        invalidateParent();

        setWidth(texture.getRegionWidth());
        setHeight(texture.getRegionHeight());

        return this;
    }
}
