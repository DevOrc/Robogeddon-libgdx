package com.noahcharlton.robogeddon.block.drone;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class SpawnPadBlock extends Block implements HasTileEntity {

    private static final float POWER_USAGE = 4f;

    public SpawnPadBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requiredBlocks = List.of(Blocks.relayBlock);
        requirements = List.of(Items.iron.stack(100), Items.circuit.stack(10));
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                FloatUtils.asIntString(POWER_USAGE)
        };
    }

    @Override
    public String getDisplayName() {
        return "Spawn Pad";
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new SpawnPadTileEntity(tile, POWER_USAGE);
    }
}
