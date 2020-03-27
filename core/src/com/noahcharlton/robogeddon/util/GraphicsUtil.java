package com.noahcharlton.robogeddon.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GraphicsUtil {

    public static void drawRotated(SpriteBatch batch, Texture texture, float x, float y, float degrees) {
        int width = texture.getWidth();
        int height = texture.getHeight();

        batch.draw(texture, x, y, width / 2f, height / 2f, width, height,
                1, 1, degrees, 0, 0, width, height, false, false);
    }

}
