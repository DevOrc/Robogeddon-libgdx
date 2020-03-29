package com.noahcharlton.robogeddon.world;

public class TileUpdate {

    final int x;
    final int y;
    final String block;

    TileUpdate(Tile tile) {
        this.x = tile.getX();
        this.y = tile.getY();

        this.block = tile.hasBlock() ? tile.getBlock().getTypeID():null;
    }
}
