package com.noahcharlton.robogeddon.world.floor;

import com.noahcharlton.robogeddon.world.item.Item;

public class MineableFloor extends Floor {

    private final Item ore;

    public MineableFloor(String id, String displayName, Item ore) {
        super(id, displayName);

        this.ore = ore;
    }

    public Item getOre() {
        return ore;
    }
}
