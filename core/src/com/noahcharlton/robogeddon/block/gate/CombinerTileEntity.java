package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.duct.ItemDuctTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.GenericItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class CombinerTileEntity extends TileEntity implements HasInventory {

    private ItemBuffer buffer = new GenericItemBuffer(1);
    private int time;

    private Direction inputDirection = Direction.NORTH;

    public CombinerTileEntity(Tile rootTile) {
        super(rootTile);
    }

    @Override
    public void update() {
        if(world.isClient())
            return;

        time--;

        if(time == 0 && buffer.getAmount() == 0){
            gotoNextDirection();
        }
    }

    private void gotoNextDirection() {
        time = 30;

        switch(inputDirection) {
            case NORTH:
                inputDirection = Direction.EAST;
                if(canInput())
                    break;
            case EAST:
                inputDirection = Direction.SOUTH;
                if(canInput())
                    break;
            case SOUTH:
                inputDirection = Direction.WEST;
                if(canInput())
                    break;
            case WEST:
                inputDirection = Direction.NORTH;
                if(canInput())
                    break;

                //Since this is the last one, we should only move over once to prevent
                //an infinite loop from forming (because there might be NO connections)
                inputDirection = Direction.EAST;
        }
    }

    private boolean canInput() {
        var tile = rootTile.getNeighbor(inputDirection);

        if(tile.getTileEntity() instanceof ItemDuctTileEntity){
            return ((ItemDuctTileEntity) tile.getTileEntity()).getDirection() == inputDirection.flip();
        }

        return false;
    }

    @Override
    public boolean acceptItem(Item item, Direction from) {
        if(inputDirection != from || time > 5)
            return false;

        if(buffer.acceptItem(item)){
            gotoNextDirection();
            return true;
        }

        return false;
    }

    @Override
    public Item retrieveItem(boolean simulate, Direction to) {
        if(simulate){
            return buffer.getAmount() > 0 ? buffer.currentItem() : null;
        }

        return buffer.retrieveItem();
    }

    @Override
    public String[] getInventoryDetails() {
        return new String[0];
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {
        buffer = buffers[0];
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return new ItemBuffer[]{buffer};
    }
}
