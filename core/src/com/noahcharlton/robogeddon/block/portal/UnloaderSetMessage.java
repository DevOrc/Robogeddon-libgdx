package com.noahcharlton.robogeddon.block.portal;

import com.noahcharlton.robogeddon.block.tileentity.CustomTileEntityMessage;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class UnloaderSetMessage extends CustomTileEntityMessage {

    private final Item item;

    public UnloaderSetMessage(Tile tile, Item item) {
        super(tile);

        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
