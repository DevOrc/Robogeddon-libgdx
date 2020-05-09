package com.noahcharlton.robogeddon.block.gate;

import com.badlogic.gdx.utils.Array;
import com.noahcharlton.robogeddon.block.tileentity.CustomTileEntityMessage;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntitySelectable;
import com.noahcharlton.robogeddon.block.tileentity.inventory.GenericItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemTypeBuffer;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

/**
 * Outputs selected items to the east or west
 */
public class SorterTileEntity extends TileEntity implements HasInventory, TileEntitySelectable {

    public static final String subMenuID = "sorter_tile_entity";

    @Side(Side.SERVER)
    private final ItemBuffer eastWestItemBuffer = new GenericItemBuffer(1);
    @Side(Side.SERVER)
    private int eastWestTime = 0;

    @Side(Side.SERVER)
    private final ItemBuffer northSouthItemBuffer = new GenericItemBuffer(1);
    @Side(Side.SERVER)
    private int northSouthTime = 0;

    private Array<ItemBuffer> sortedTypes = new Array<>();

    public SorterTileEntity(Tile rootTile) {
        super(rootTile);
    }

    @Override
    public void update() {
        if(world.isClient()){
            return;
        }

        if(eastWestTime > 0){
            eastWestTime--;
        }

        if(northSouthTime > 0){
            northSouthTime--;
        }
    }

    @Override
    public void onCustomMessageReceived(CustomTileEntityMessage message) {
        if(message instanceof SorterSelectionMessage){
            sortedTypes.clear();

            for(Item item: ((SorterSelectionMessage) message).getSelectedItems()){
                sortedTypes.add(new ItemTypeBuffer(item));
            }

            dirty = true;
        }else{
            super.onCustomMessageReceived(message);
        }
    }

    @Override
    public boolean acceptItem(Item item, Direction from) {
        ItemBuffer buffer = getBufferForItem(item);

        if(buffer.acceptItem(item)){
            if(buffer == northSouthItemBuffer){
                northSouthTime = 30;
            }else{
                eastWestTime = 30;
            }

            return true;
        }

        return false;
    }

    private ItemBuffer getBufferForItem(Item item) {
        for(ItemBuffer buffer : getBuffers()){
            if(buffer.currentItem() == item)
                return eastWestItemBuffer;
        }

        return northSouthItemBuffer;
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {
        this.sortedTypes = new Array<>(buffers);
    }

    @Override
    public Item retrieveItem(boolean simulate, Direction to) {
        if(to.isNorthSouth()){
            return retrieveItem(simulate, northSouthItemBuffer, northSouthTime);
        }

        return retrieveItem(simulate, eastWestItemBuffer, eastWestTime);
    }

    private Item retrieveItem(boolean simulate, ItemBuffer buffer, int time) {
        if(time > 0)
            return null;

        if(simulate){
            return buffer.getAmount() > 0 ? buffer.currentItem() : null;
        }

        return buffer.retrieveItem();
    }

    @Override
    public String[] getInventoryDetails() {
        return new String[0];
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return sortedTypes.toArray(ItemBuffer.class);
    }

    @Override
    public String getSubMenuID() {
        return subMenuID;
    }
}
