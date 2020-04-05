package com.noahcharlton.robogeddon.block.tileentity;

import com.noahcharlton.robogeddon.world.Tile;

public interface HasTileEntity {

    TileEntity createTileEntity(Tile tile);
}
