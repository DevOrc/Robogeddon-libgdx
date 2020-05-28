package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class SplitterBlock extends Block implements HasTileEntity {

    public SplitterBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requirements = List.of(Items.iron.stack(10));
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
