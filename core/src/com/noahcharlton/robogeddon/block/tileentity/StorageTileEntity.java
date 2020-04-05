package com.noahcharlton.robogeddon.block.tileentity;

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
    public Item retrieveItem() {
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
