package com.noahcharlton.robogeddon.world.item;

import com.noahcharlton.robogeddon.message.Message;

public class InventorySyncMessage implements Message {

    private final String[] ids;
    private final int[] amounts;

    public InventorySyncMessage(Inventory inventory) {
        var entries = inventory.getMap().entrySet();
        ids = new String[entries.size()];
        amounts = new int[entries.size()];

        var iterator = entries.iterator();
        for(int i = 0; i < entries.size(); i++) {
            var itemStack = iterator.next();

            ids[i] = itemStack.getKey();
            amounts[i] = itemStack.getValue();
        }
    }

    public int[] getAmounts() {
        return amounts;
    }

    public String[] getIds() {
        return ids;
    }
}
