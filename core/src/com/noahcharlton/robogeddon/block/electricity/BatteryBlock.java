package com.noahcharlton.robogeddon.block.electricity;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.BatteryTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class BatteryBlock extends Block implements HasTileEntity {

    private static final float CAPACITY = 1000f;

    private List<Block> requiredBlocks;
    private List<ItemStack> requiredItems;

    public BatteryBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requiredBlocks = List.of(Blocks.relayBlock);
        requiredItems =  List.of(Items.iron.stack(45), Items.circuit.stack(2));
    }

    @Override
    public List<ItemStack> getRequirements() {
        return requiredItems;
    }

    @Override
    public List<Block> getRequiredBlocks() {
        return requiredBlocks;
    }

    @Override
    public String getDisplayName() {
        return "Battery";
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                FloatUtils.asIntString(CAPACITY)
        };
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new BatteryTileEntity(tile, CAPACITY);
    }
}
