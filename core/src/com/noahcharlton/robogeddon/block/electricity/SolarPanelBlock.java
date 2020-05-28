package com.noahcharlton.robogeddon.block.electricity;

import com.noahcharlton.robogeddon.block.Block;
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

    public SolarPanelBlock(String id) {
        super(id);
    }

    @Override
    public List<ItemStack> getRequirements() {
        return List.of(Items.iron.stack(5), Items.circuit.stack(1));
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
