package com.noahcharlton.robogeddon.block.drone;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;

public class SpawnPadBlock extends Block implements HasTileEntity {

    public SpawnPadBlock(String id) {
        super(id);
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
        return new SpawnTileEntity(tile, 4f);
    }
}
