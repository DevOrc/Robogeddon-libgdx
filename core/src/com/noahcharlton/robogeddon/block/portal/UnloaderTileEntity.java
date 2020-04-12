package com.noahcharlton.robogeddon.block.portal;

import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.ItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntitySelectable;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Inventory;
import com.noahcharlton.robogeddon.world.item.Item;
import com.noahcharlton.robogeddon.world.item.Items;

public class UnloaderTileEntity extends TileEntity implements HasInventory, TileEntitySelectable {

    private static final int TIME = 60;
    public static final String subMenuID = "unloader_tile_entity";

    private Inventory inventory;
    private int tick = 0;

    public UnloaderTileEntity(Tile rootTile) {
        super(rootTile);

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
    public boolean acceptItem(Item item) {
        return false;
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {}

    @Override
    public Item retrieveItem(boolean simulate) {
        if(tick >= TIME && inventory.useItemIfEnough(Items.rock, 1)){
            tick = 0;
            return Items.rock;
        }
        return null;
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return new ItemBuffer[0];
    }

    @Override
    public String getSubMenuID() {
        return subMenuID;
    }
}
