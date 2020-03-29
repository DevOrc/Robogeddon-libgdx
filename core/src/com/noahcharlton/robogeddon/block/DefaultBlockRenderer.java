package com.noahcharlton.robogeddon.block;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;

@Side(Side.CLIENT)
public class DefaultBlockRenderer implements BlockRenderer {

    private final Texture texture;

    public DefaultBlockRenderer(Block block) {
        this.texture = new Texture(block.getTypeID() + ".png");
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        float x = tile.getPixelX();
        float y = tile.getPixelY();

        batch.draw(texture, x, y);
    }
}
