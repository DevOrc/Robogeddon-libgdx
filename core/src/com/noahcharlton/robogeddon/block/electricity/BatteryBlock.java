package com.noahcharlton.robogeddon.block.electricity;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.BatteryTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.Tile;

public class BatteryBlock extends Block implements HasTileEntity {

    private static final float CAPACITY = 1000f;

    public BatteryBlock(String id) {
        super(id);
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
