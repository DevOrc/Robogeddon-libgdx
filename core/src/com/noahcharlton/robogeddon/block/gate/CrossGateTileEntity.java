package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.GenericItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class CrossGateTileEntity extends TileEntity implements HasInventory {

    @Side(Side.SERVER)
    private ItemBuffer eastWestItemBuffer = new GenericItemBuffer(1);
    @Side(Side.SERVER)
    private int eastWestTime = 0;

    @Side(Side.SERVER)
    private ItemBuffer northSouthItemBuffer = new GenericItemBuffer(1);
    @Side(Side.SERVER)
    private int northSouthTime = 0;

    public CrossGateTileEntity(Tile rootTile) {
        super(rootTile);
    }

    @Override
    public void update() {
        if(world.isClient()){
            return;
        }

        if(eastWestTime > 0){
            eastWestTime--;
        }

        if(northSouthTime > 0){
            northSouthTime--;
        }
    }

    @Override
    public boolean acceptItem(Item item, Direction from) {
        var buffer = from.isNorthSouth() ? northSouthItemBuffer : eastWestItemBuffer;

        if(buffer.acceptItem(item)){
            if(from.isNorthSouth()){
                northSouthTime = 30;
            }else{
                eastWestTime = 30;
            }

            return true;
        }

        return false;
    }

    @Override
    public Item retrieveItem(boolean simulate, Direction to) {
        var buffer = to.isNorthSouth() ? northSouthItemBuffer : eastWestItemBuffer;
        var time = to.isNorthSouth() ? northSouthTime : eastWestTime;

        if(time > 0){
            return null;
        }

        if(simulate){
            return buffer.getAmount() > 0 ? buffer.currentItem() : null;
        }else{
            return buffer.retrieveItem();
        }
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {
        northSouthItemBuffer = buffers[0];
        eastWestItemBuffer = buffers[1];
    }

    @Override
    public String[] getInventoryDetails() {
        return new String[0];
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return new ItemBuffer[]{northSouthItemBuffer, eastWestItemBuffer};
    }
}
