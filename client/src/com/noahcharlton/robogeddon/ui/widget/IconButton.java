package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.ui.Scale;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.TextureBackground;

public class IconButton extends Button {

    private static final float BORDER = 8;
    private TextureRegion texture;

    public IconButton(TextureRegion texture) {
        this.texture = texture;

        setDefaultBackground(new TextureBackground(UIAssets.iconButton));
        setOnHover(new TextureBackground(UIAssets.iconButtonHover));
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() + (BORDER / 2f), getY() + (BORDER / 2f),
                getWidth() - BORDER, getHeight() - BORDER);
    }

    @Override
    public void layout() {
        float size = (BORDER + (texture.getRegionWidth() * Scale.scale));
        setSize(size, size);
    }

    public IconButton setTexture(TextureRegion texture) {
        this.texture = texture;
        invalidateParent();

        return this;
    }
}
