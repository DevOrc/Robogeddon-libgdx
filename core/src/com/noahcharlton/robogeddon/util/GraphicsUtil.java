package com.noahcharlton.robogeddon.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GraphicsUtil {

    public static void drawRotated(SpriteBatch batch, TextureRegion texture, float x, float y, float degrees) {
        int width = texture.getRegionWidth();
        int height = texture.getRegionHeight();

        batch.draw(texture, x, y, width / 2f, height / 2f, width, height, 1f,
                1f, degrees);
    }

}
