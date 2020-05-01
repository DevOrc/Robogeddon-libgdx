package com.noahcharlton.robogeddon.block;

public class WallBlock extends Block {

    public WallBlock(String id) {
        super(id);
    }

    @Override
    public float getHardness() {
        return 2f;
    }

    @Override
    public String getDisplayName() {
        return "Rock Wall";
    }
}
