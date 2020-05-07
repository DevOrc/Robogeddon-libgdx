package com.noahcharlton.robogeddon.block.portal;

import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.*;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.SingleItemBuffer;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Inventory;
import com.noahcharlton.robogeddon.world.item.Item;
import com.noahcharlton.robogeddon.world.item.Items;

public class UnloaderTileEntity extends TileEntity implements HasInventory, TileEntitySelectable {

    private static final int TIME = 60;
    public static final String subMenuID = "unloader_tile_entity";

    private ItemBuffer item;
    private Inventory inventory;
    private int tick = 0;

    public UnloaderTileEntity(Tile rootTile) {
        super(rootTile);
        item = new SingleItemBuffer(Items.rock, 0);

        if(world.isServer())
            this.inventory = ((ServerWorld) world).getInventory();
    }

    @Override
    public void update() {
        if(world.isServer() && tick <= TIME && hasPortal())
            tick++;
    }

    private boolean hasPortal() {
        for(Tile tile: getRootTile().getNeighbors()){
            if(tile != null && tile.isBlockOrMulti(Blocks.inventoryPortal)){
                return true;
            }
        }

        return false;
    }

    @Override
    public void onCustomMessageReceived(CustomTileEntityMessage message) {
        if(message instanceof UnloaderSetMessage){
            item = new SingleItemBuffer(((UnloaderSetMessage) message).getItem(), 0);
            dirty = true;
        }else{
            super.onCustomMessageReceived(message);
        }
    }

    @Override
    public boolean acceptItem(Item item) {
        return false;
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {
        this.item = buffers[0];
    }

    @Override
    public Item retrieveItem(boolean simulate, Direction to) {
        if(simulate){
            if(tick < TIME || inventory.getItem(item.currentItem()) <= 0){
                return null;
            }

            return item.currentItem();
        }

        if(tick >= TIME && inventory.useItemIfEnough(item.currentItem(), 1)){
            tick = 0;
            return item.currentItem();
        }
        return null;
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return new ItemBuffer[]{item};
    }

    @Override
    public String getSubMenuID() {
        return subMenuID;
    }

    @Override
    public String[] getInventoryDetails() {
        return new String[]{"Unloading: " + item.currentItem().getDisplayName()};
    }
}
