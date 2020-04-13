package com.noahcharlton.robogeddon.block.tileentity;

import com.noahcharlton.robogeddon.message.Message;
import com.noahcharlton.robogeddon.world.Tile;

public class CustomTileEntityMessage implements Message {

    private final int x;
    private final int y;

    public CustomTileEntityMessage(Tile tile) {
        x = tile.getX();
        y = tile.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
