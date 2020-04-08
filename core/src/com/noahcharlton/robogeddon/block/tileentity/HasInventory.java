package com.noahcharlton.robogeddon.block.tileentity;

import com.noahcharlton.robogeddon.util.Direction;
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

    /**
     * Used by transport tile entities
     * to determine if the can be connected
     */
    @Side(Side.SERVER)
    default boolean canConnect(Direction from, Direction beltDirection){
        return true;
    }

}
