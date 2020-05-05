package com.noahcharlton.robogeddon.block.miner;

import com.noahcharlton.robogeddon.block.tileentity.inventory.GenericItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.StorageTileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.floor.MineableFloor;
import com.noahcharlton.robogeddon.world.item.Item;

public class MinerTileEntity extends StorageTileEntity {

    static final int TIME = 120;

    private final Item item;

    private int time = 0;

    public MinerTileEntity(Tile rootTile) {
        super(rootTile, new GenericItemBuffer(10));

        if(rootTile.getUpperFloor() instanceof MineableFloor){
            item = ((MineableFloor) rootTile.getUpperFloor()).getOre();
        }else if(rootTile.getFloor() instanceof MineableFloor){
            item = ((MineableFloor) rootTile.getFloor()).getOre();
        }else{
            throw new RuntimeException("Cannot place miner on non-mineable floor!");
        }
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
