package com.noahcharlton.robogeddon.block;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;

@Side(Side.CLIENT)
public class DefaultBlockRenderer implements BlockRenderer {

    private TextureRegion texture;

    public DefaultBlockRenderer(Block block) {
        var path = "blocks/" + block.getTypeID();

        Core.assets.registerTexture(path).setOnLoad(t -> texture = t);
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        float x = tile.getPixelX();
        float y = tile.getPixelY();

        batch.draw(texture, x, y);
    }
}
