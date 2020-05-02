package com.noahcharlton.robogeddon.block.tileentity.inventory;

import com.noahcharlton.robogeddon.world.item.Item;

public class SingleItemBuffer implements ItemBuffer{

    private final Item item;
    private final int capacity;

    private int amount;

    public SingleItemBuffer(Item item, int capacity) {
        this.item = item;
        this.capacity = capacity;
    }

    @Override
    public boolean acceptItem(Item item) {
        if(amount >= capacity || item != this.item)
            return false;

        amount++;
        return true;
    }

    @Override
    public Item retrieveItem() {
        if(amount <= 0)
            return null;

        amount--;
        return item;
    }

    @Override
    public boolean isFull() {
        return amount >= capacity;
    }

    @Override
    public Item currentItem() {
        return item;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public ItemBuffer copy() {
        var buffer = new SingleItemBuffer(this.item, this.capacity);
        buffer.amount = this.amount;

        return buffer;
    }

    @Override
    public String toString() {
        return "SingleItemBuffer{" +
                "item=" + item +
                ", amount=" + amount +
                '}';
    }
}
