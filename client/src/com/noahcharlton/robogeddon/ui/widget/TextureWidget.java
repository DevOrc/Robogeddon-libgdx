package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.ui.Scale;

public class TextureWidget extends NoChildrenWidget{

    private TextureRegion texture;

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void layout() {
        setWidthScaled(texture.getRegionWidth() * Scale.scale);
        setHeightScaled(texture.getRegionHeight() * Scale.scale);
    }

    public TextureWidget setTexture(TextureRegion texture) {
        this.texture = texture;
        invalidateParent();

        setWidth(texture.getRegionWidth());
        setHeight(texture.getRegionHeight());

        return this;
    }
}
