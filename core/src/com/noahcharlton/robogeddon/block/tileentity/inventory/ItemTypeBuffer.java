package com.noahcharlton.robogeddon.block.tileentity.inventory;

import com.noahcharlton.robogeddon.world.item.Item;

/**
 * An item buffer used to communicate item types between the client and the server
 */
public class ItemTypeBuffer implements ItemBuffer{

    private final Item item;

    public ItemTypeBuffer(Item item) {
        this.item = item;
    }

    @Override
    public boolean acceptItem(Item item) {
        return false;
    }

    @Override
    public Item retrieveItem() {
        return null;
    }

    @Override
    public Item currentItem() {
        return item;
    }

    @Override
    public int getAmount() {
        return 0;
    }

    @Override
    public ItemBuffer copy() {
        return new ItemTypeBuffer(item);
    }

    @Override
    public boolean isFull() {
        return true;
    }
}
