package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class SorterBlock extends Block implements HasTileEntity {

    public SorterBlock(String id) {
        super(id);
    }

    @Override
    public List<ItemStack> getRequirements() {
        return List.of(Items.iron.stack(10), Items.circuit.stack(2));
    }

    @Override
    public String getDisplayName() {
        return "Sorter";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new SorterTileEntity(tile);
    }
}
