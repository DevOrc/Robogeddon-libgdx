package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.message.Message;

public class BuildBlockMessage implements Message {

    private String blockID;
    private int tileX;
    private int tileY;

    public BuildBlockMessage(Tile tile, Block block) {
        if(block != null)
            this.blockID = block.getTypeID();
        this.tileX = tile.getX();
        this.tileY = tile.getY();
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public String getBlockID() {
        return blockID;
    }
}
