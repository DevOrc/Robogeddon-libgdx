package com.noahcharlton.robogeddon.block.miner;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.HasTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.world.Tile;

public class MinerBlock extends Block implements HasTileEntity {

    public MinerBlock(String id) {
        super(id);
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new MinerTileEntity(tile);
    }
}
