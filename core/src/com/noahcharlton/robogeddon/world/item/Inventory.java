package com.noahcharlton.robogeddon.world.item;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.message.Message;

import java.util.HashMap;

public class Inventory {

    private final HashMap<String, Integer> inventory = new HashMap<>();

    private boolean dirty;

    public Inventory() {
        Core.items.values().forEach(item -> inventory.put(item.getTypeID(), 0));
    }

    public Message createSyncMessage() {
        return new InventorySyncMessage(this);
    }

    public void setItem(Item item, int amount){
        inventory.put(item.getTypeID(), Math.max(amount, 0));
        dirty = true;

        Log.trace("Set " + item.getTypeID() + " to " + amount);
    }

    public void changeItem(ItemStack stack){
        changeItem(stack.getItem(), stack.getAmount());
    }

    public boolean useItemIfEnough(Item item, int amount){
        if(getItem(item) >= amount){
            changeItem(item, -amount);
            return true;
        }

        return false;
    }

    public void changeItem(Item item, int delta){
        setItem(item, getItem(item) + delta);
    }

    public int getItem(Item item){
        return inventory.getOrDefault(item.getTypeID(), 0);
    }

    public boolean isDirty() {
        return dirty;
    }

    public void clean() {
        dirty = false;
    }

    public HashMap<String, Integer> getMap() {
        return inventory;
    }
}
