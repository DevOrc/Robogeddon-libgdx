package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.tileentity.CustomTileEntityMessage;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class SorterSelectionMessage extends CustomTileEntityMessage {

    private final Item[] selectedItems;

    public SorterSelectionMessage(Tile tile, Item[] items) {
        super(tile);

        this.selectedItems = items;
    }

    public Item[] getSelectedItems() {
        return selectedItems;
    }
}
