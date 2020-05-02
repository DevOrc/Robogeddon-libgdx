package com.noahcharlton.robogeddon.block.electricity;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;

public class RelayBlock extends Block implements HasTileEntity {

    public RelayBlock(String id) {
        super(id);
    }

    @Override
    public String getDisplayName() {
        return "Relay";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new RelayTileEntity(tile);
    }
}
