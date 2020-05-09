package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.duct.ItemDuctTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.CustomTileEntityMessage;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntitySelectable;
import com.noahcharlton.robogeddon.block.tileentity.inventory.GenericItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class FlowGateTileEntity extends TileEntity implements HasInventory, TileEntitySelectable {

    public static final String subMenuID = "flow_gate_tile_entity";

    private ItemBuffer itemBuffer = new GenericItemBuffer(1);

    private boolean overflowNorth;
    private boolean overflowEast;
    private boolean overflowSouth;
    private boolean overflowWest;
    int time = 0;

    public FlowGateTileEntity(Tile rootTile) {
        super(rootTile);
    }

    @Override
    public void update() {
        if(world.isServer() && time > 0){
            time--;
        }
    }

    @Override
    public void onCustomMessageReceived(CustomTileEntityMessage message) {
        if(message instanceof FlowGateSelectionMessage) {
            var selection = (FlowGateSelectionMessage) message;

            overflowNorth = selection.overflowNorth;
            overflowEast = selection.overflowEast;
            overflowSouth = selection.overflowSouth;
            overflowWest = selection.overflowWest;

            dirty = true;
        } else {
            super.onCustomMessageReceived(message);
        }
    }

    @Override
    public boolean acceptItem(Item item, Direction from) {
        if(itemBuffer.acceptItem(item)){
            time = 25;
            return true;
        }

        return false;
    }

    @Override
    public Item retrieveItem(boolean simulate, Direction to) {
        if(!canOutput(to) || time > 0){
            return null;
        }

        if(simulate){
            return itemBuffer.getAmount() > 0 ? itemBuffer.currentItem() : null;
        }

        return itemBuffer.retrieveItem();
    }

    private boolean canOutput(Direction to) {
        if(isDirectionOverflow(to)){
            return isNormalFull();
        }

        return true;
    }

    private boolean isNormalFull() {
        if(!overflowNorth && canReceiveItems(Direction.NORTH)){
            return false;
        }else if(!overflowEast && canReceiveItems(Direction.EAST)){
            return false;
        }else if(!overflowSouth && canReceiveItems(Direction.SOUTH)){
            return false;
        }

        return !overflowWest && !canReceiveItems(Direction.WEST);
    }

    private boolean canReceiveItems(Direction direction) {
        var tile = getRootTile().getNeighbor(direction);

        if(tile == null || tile.getTileEntity() == null || !(tile.getTileEntity() instanceof ItemDuctTileEntity))
            return false;

        var itemDuct = (ItemDuctTileEntity) tile.getTileEntity();

        return !itemDuct.isFull() && itemDuct.getDirection().flip() != direction;
    }

    private boolean isDirectionOverflow(Direction to) {
        switch(to){
            case NORTH:
                return overflowNorth;
            case EAST:
                return overflowEast;
            case SOUTH:
                return overflowSouth;
            case WEST:
                return overflowWest;
        }
        return false;
    }

    @Override
    public void receiveData(float[] data) {
        overflowNorth = toBoolean(data[0]);
        overflowEast = toBoolean(data[1]);
        overflowSouth = toBoolean(data[2]);
        overflowWest = toBoolean(data[3]);
    }

    @Override
    public float[] getData() {
        return new float[]{
                toFloat(overflowNorth),
                toFloat(overflowEast),
                toFloat(overflowSouth),
                toFloat(overflowWest)
        };
    }

    private boolean toBoolean(float val){
        return val == 1f;
    }

    private float toFloat(boolean val) {
        return val ? 1f : 0f;
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {
        itemBuffer = buffers[0];
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return new ItemBuffer[]{itemBuffer};
    }

    @Override
    public String[] getInventoryDetails() {
        return new String[0];
    }

    @Override
    public String getSubMenuID() {
        return subMenuID;
    }

    public boolean isOverflowEast() {
        return overflowEast;
    }

    public boolean isOverflowNorth() {
        return overflowNorth;
    }

    public boolean isOverflowSouth() {
        return overflowSouth;
    }

    public boolean isOverflowWest() {
        return overflowWest;
    }
}
