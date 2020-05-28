package com.noahcharlton.robogeddon.block.portal;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class UnloaderBlock extends Block implements HasTileEntity {

    public UnloaderBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requirements = List.of(Items.iron.stack(10), Items.circuit.stack(1));
    }

    @Override
    public String getDisplayName() {
        return "Unloader";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new UnloaderTileEntity(tile);
    }
}
