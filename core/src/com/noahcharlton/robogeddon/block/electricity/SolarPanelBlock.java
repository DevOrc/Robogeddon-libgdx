package com.noahcharlton.robogeddon.block.electricity;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.GeneratorTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.Tile;

public class SolarPanelBlock extends Block implements HasTileEntity {

    private static final float PRODUCTION_RATE = 1f;

    public SolarPanelBlock(String id) {
        super(id);
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
