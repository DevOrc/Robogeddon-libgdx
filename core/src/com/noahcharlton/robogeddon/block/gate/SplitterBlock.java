package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;

public class SplitterBlock extends Block implements HasTileEntity {

    public SplitterBlock(String id) {
        super(id);
    }

    @Override
    public String getDisplayName() {
        return "Splitter";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new SplitterTileEntity(tile);
    }
}
