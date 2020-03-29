package com.noahcharlton.robogeddon.block;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;

@Side(Side.CLIENT)
public interface BlockRenderer {

    void render(SpriteBatch batch, Tile tile);
}
