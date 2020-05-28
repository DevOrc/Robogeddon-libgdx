package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class CombinerBlock extends Block implements HasTileEntity {

    public CombinerBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requirements = List.of(Items.iron.stack(5), Items.circuit.stack(1));
    }

    @Override
    public String getDisplayName() {
        return "Combiner";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new CombinerTileEntity(tile);
    }
}
