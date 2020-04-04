package com.noahcharlton.robogeddon.block;

public class TestBlock extends Block{

    public TestBlock(String id) {
        super(id);
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public int getHeight() {
        return 2;
    }
}
