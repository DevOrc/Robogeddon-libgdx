package com.noahcharlton.robogeddon.block;

import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.util.Side;

public abstract class Block implements HasID {

    private final String id;

    @Side(Side.CLIENT)
    private BlockRenderer renderer;

    public Block(String id) {
        this.id = id;
    }

    @Side(Side.CLIENT)
    public void initRenderer(){
        renderer = new DefaultBlockRenderer(this);
    }

    public BlockRenderer getRenderer() {
        return renderer;
    }

    public String getTypeID() {
        return id;
    }
}
