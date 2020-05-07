package com.noahcharlton.robogeddon.block.tileentity.inventory;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class StorageTileEntity extends TileEntity implements HasInventory{

    protected ItemBuffer itemBuffer;

    public StorageTileEntity(Tile rootTile, ItemBuffer itemBuffer) {
        super(rootTile);

        this.itemBuffer = itemBuffer;
    }

    @Override
    public boolean acceptItem(Item item) {
        if(itemBuffer.acceptItem(item)){
            dirty = true;
            return true;
        }

        return false;
    }

    @Override
    public Item retrieveItem(boolean simulate, Direction to) {
        if(simulate){
            return itemBuffer.getAmount() > 0 ? itemBuffer.currentItem() : null;
        }

        var item = itemBuffer.retrieveItem();

        if(item != null){
            dirty = true;
            return item;
        }

        return null;
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {
        if(buffers.length != 1)
            throw new UnsupportedOperationException();

        this.itemBuffer = buffers[0];
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return new ItemBuffer[]{itemBuffer};
    }
}
