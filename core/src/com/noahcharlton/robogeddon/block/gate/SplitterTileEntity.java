package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.duct.ItemDuctTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.GenericItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

/**
 * The splitter will output to one direction at a time. Once an item is accepted, it will wait 25 ticks,
 * then give the designated side 5 ticks to pull an item. If an item is pulled (or after 5 ticks or nothing)
 * the splitter will move to the next side. Also this tile entity does NOT send its items to the client
 */
public class SplitterTileEntity extends TileEntity implements HasInventory {

    @Side(Side.SERVER)
    private final ItemBuffer itemBuffer = new GenericItemBuffer(1);

    @Side(Side.SERVER)
    private Direction direction = Direction.NORTH;
    @Side(Side.SERVER)
    private int tick = 0;

    public SplitterTileEntity(Tile rootTile) {
        super(rootTile);
    }

    @Override
    public void update() {
        if(world.isClient() || itemBuffer.getAmount() == 0)
            return;

        if(tick > 0){
            tick--;
        }else{
            gotoNextDirection();
        }
    }

    private void gotoNextDirection() {
        tick = 30;

        switch(direction) {
            case NORTH:
                direction = Direction.EAST;
                if(canOutput())
                    break;
            case EAST:
                direction = Direction.SOUTH;
                if(canOutput())
                    break;
            case SOUTH:
                direction = Direction.WEST;
                if(canOutput())
                    break;
            case WEST:
                direction = Direction.NORTH;
                if(canOutput())
                    break;

                //Since this is the last one, we should only move over once to prevent
                //an infinite loop from forming (because there might be NO connections)
                direction = Direction.EAST;
        }
    }

    private boolean canOutput() {
        var tile = rootTile.getNeighbor(direction);

        //If its an item duct, make sure they aren't pushing into this tile!
        if(tile.getTileEntity() instanceof ItemDuctTileEntity){
            return ((ItemDuctTileEntity) tile.getTileEntity()).getDirection() != direction.flip();
        }

        return tile.getTileEntity() instanceof HasInventory;
    }

    @Override
    public boolean acceptItem(Item item, Direction from) {
        if(itemBuffer.acceptItem(item)) {
            return true;
        }

        return false;
    }

    @Override
    public Item retrieveItem(boolean simulate, Direction to) {
        if(itemBuffer.getAmount() == 0 || tick > 5)
            return null;

        if(to != direction)
            return null;

        if(simulate) {
            return itemBuffer.currentItem();
        }else{
            gotoNextDirection();
            return itemBuffer.retrieveItem();
        }
    }

    @Override
    public String[] getInventoryDetails() {
        return new String[0];
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return new ItemBuffer[0];
    }
}
