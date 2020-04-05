package com.noahcharlton.robogeddon.ui.widget;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.TextureBackground;

public class IconButton extends Button {

    private static final int SIZE = 40;

    private TextureRegion texture;

    public IconButton(TextureRegion texture) {
        this.texture = texture;

        setSize(SIZE, SIZE);
        setMinSize(SIZE, SIZE);

        setDefaultBackground(new TextureBackground(UIAssets.iconButton));
        setOnHover(new TextureBackground(UIAssets.iconButtonHover));
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() + 4, getY() + 4);
    }

    @Override
    public void layout() {
        setSize(SIZE, SIZE);
        setMinSize(SIZE, SIZE);
    }

    public IconButton setTexture(TextureRegion texture) {
        this.texture = texture;
        invalidateParent();

        return this;
    }
}
