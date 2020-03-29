package com.noahcharlton.robogeddon.world;

public class TileUpdate {

    final int x;
    final int y;
    final String block;
    final String floor;

    TileUpdate(Tile tile) {
        this.x = tile.getX();
        this.y = tile.getY();

        this.floor = tile.getFloor().getTypeID();
        this.block = tile.hasBlock() ? tile.getBlock().getTypeID():null;
    }
}
