package com.noahcharlton.robogeddon.block.tileentity;

import com.noahcharlton.robogeddon.world.item.Item;

public class GenericItemBuffer implements ItemBuffer {

    private final int capacity;

    private Item item;
    private int amount;

    public GenericItemBuffer(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean acceptItem(Item item) {
        if(amount <= 0){
            this.item = item;
        }

        if(amount >= capacity || this.item != item)
            return false;

        amount++;
        return true;
    }

    @Override
    public Item retrieveItem() {
        if(amount <= 0 || item == null)
            return null;

        amount--;
        var item = this.item;

        if(amount == 0){
            this.item = null;
        }

        return item;
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
        var buffer = new GenericItemBuffer(capacity);
        buffer.amount = this.amount;
        buffer.item = this.item;

        return buffer;
    }

    @Override
    public String toString() {
        return "GenericItemBuffer{" +
                "item=" + item +
                ", amount=" + amount +
                '}';
    }
}
