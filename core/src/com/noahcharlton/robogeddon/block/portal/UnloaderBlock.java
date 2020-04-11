package com.noahcharlton.robogeddon.block.portal;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.HasTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.world.Tile;

public class UnloaderBlock extends Block implements HasTileEntity {

    public UnloaderBlock(String id) {
        super(id);
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new UnloaderTileEntity(tile);
    }
}
