package com.noahcharlton.robogeddon.world.floor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.world.Tile;

public class OreFloor extends Floor {

    public OreFloor(String id) {
        super(id);
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        Floors.rock.render(batch, tile);

        super.render(batch, tile);
    }
}
