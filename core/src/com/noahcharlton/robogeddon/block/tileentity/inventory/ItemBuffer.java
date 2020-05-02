package com.noahcharlton.robogeddon.block.tileentity.inventory;

import com.noahcharlton.robogeddon.world.item.Item;

public interface ItemBuffer {

    boolean acceptItem(Item item);

    Item retrieveItem();

    Item currentItem();

    int getAmount();

    ItemBuffer copy();

    boolean isFull();
}
