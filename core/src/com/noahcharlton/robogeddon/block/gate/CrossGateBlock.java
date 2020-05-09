package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;

public class CrossGateBlock extends Block implements HasTileEntity {

    public CrossGateBlock(String id) {
        super(id);
    }

    @Override
    public String getDisplayName() {
        return "Cross Gate";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new CrossGateTileEntity(tile);
    }
}
