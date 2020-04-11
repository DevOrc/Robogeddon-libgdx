package com.noahcharlton.robogeddon.block.miner;

import com.noahcharlton.robogeddon.block.tileentity.GenericItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.StorageTileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;
import com.noahcharlton.robogeddon.world.item.Items;

public class MinerTileEntity extends StorageTileEntity {

    static final int TIME = 60;

    private final Item item = Items.rock;

    private int time = 0;

    public MinerTileEntity(Tile rootTile) {
        super(rootTile, new GenericItemBuffer(10));
    }

    @Override
    public void update() {
        if(time < TIME)
            time++;

        if(time >= TIME && world.isServer() && acceptItem(item)){
            time = 0;
            dirty = true;
        }
    }

    @Override
    public float[] getData() {
        return new float[]{time};
    }

    @Override
    public void receiveData(float[] data){
        time = (int) data[0];
    }

    public float getTime() {
        return time;
    }

    public Item getItem() {
        return item;
    }
}
