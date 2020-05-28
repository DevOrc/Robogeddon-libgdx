package com.noahcharlton.robogeddon.block;

import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class WallBlock extends Block {

    public WallBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requirements = List.of(Items.rock.stack(5));
    }

    @Override
    public float getHardness() {
        return 3f;
    }

    @Override
    public String getDisplayName() {
        return "Rock Wall";
    }
}
