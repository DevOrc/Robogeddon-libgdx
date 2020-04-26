package com.noahcharlton.robogeddon.world.floor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class OreFloor extends MineableFloor {

    private final Item ore;

    public OreFloor(String id, String displayName, Item ore) {
        super(id, displayName, ore);

        this.ore = ore;
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        Floors.rock.render(batch, tile);

        super.render(batch, tile);
    }

    public Item getOre() {
        return ore;
    }
}
