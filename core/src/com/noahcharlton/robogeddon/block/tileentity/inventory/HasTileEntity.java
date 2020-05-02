package com.noahcharlton.robogeddon.block.tileentity.inventory;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.world.Tile;

public interface HasTileEntity {

    TileEntity createTileEntity(Tile tile);
}
