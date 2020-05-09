package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.tileentity.CustomTileEntityMessage;
import com.noahcharlton.robogeddon.world.Tile;

public class FlowGateSelectionMessage extends CustomTileEntityMessage {

    final boolean overflowNorth;
    final boolean overflowEast;
    final boolean overflowSouth;
    final boolean overflowWest;

    public FlowGateSelectionMessage(Tile tile, boolean north, boolean east, boolean south, boolean west) {
        super(tile);
        this.overflowNorth = north;
        this.overflowEast = east;
        this.overflowSouth = south;
        this.overflowWest = west;
    }
}
