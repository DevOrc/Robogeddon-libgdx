package com.noahcharlton.robogeddon.block.tileentity;

import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.item.Item;

public interface HasInventory {

    @Side(Side.SERVER)
    boolean acceptItem(Item item);

    @Side(Side.CLIENT)
    void setBuffers(ItemBuffer[] buffers);

    @Side(Side.SERVER)
    Item retrieveItem();

    ItemBuffer[] getBuffers();

}
