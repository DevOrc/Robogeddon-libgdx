package com.noahcharlton.robogeddon.block.tileentity;

import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.item.Item;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface HasInventory {

    @Side(Side.SERVER)
    boolean acceptItem(Item item);

    @Side(Side.CLIENT)
    void setBuffers(ItemBuffer[] buffers);

    @Side(Side.SERVER)
    Item retrieveItem(boolean simulate);

    ItemBuffer[] getBuffers();

    /**
     * Used by transport tile entities
     * to determine if the can be connected
     */
    @Side(Side.SERVER)
    default boolean canConnect(Direction from, Direction beltDirection){
        return true;
    }

    default String[] getInventoryDetails() {
        var buffers = Stream.of(getBuffers()).filter(b -> b.currentItem() != null).collect(Collectors.toList());
        String[] data = new String[buffers.size() + 1];
        data[0] = " \nInventory:";

        for(int i = 0; i < buffers.size(); i++) {
            var buffer = buffers.get(i);
            data[i + 1] = buffer.currentItem().getDisplayName() + ": " + buffer.getAmount();
        }

        return data;
    }

}
