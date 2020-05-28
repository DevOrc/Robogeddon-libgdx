package com.noahcharlton.robogeddon.block.electricity;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.GeneratorTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class SolarPanelBlock extends Block implements HasTileEntity {

    private static final float PRODUCTION_RATE = 1f;

    private List<Block> requiredBlocks;
    private List<ItemStack> requiredItems;

    public SolarPanelBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requiredBlocks = List.of(Blocks.relayBlock);
        requiredItems =  List.of(Items.iron.stack(5), Items.circuit.stack(2));
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
        return "Solar Panel";
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                FloatUtils.asIntString(PRODUCTION_RATE)
        };
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new GeneratorTileEntity(tile, PRODUCTION_RATE);
    }
}
