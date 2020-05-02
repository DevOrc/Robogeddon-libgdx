package com.noahcharlton.robogeddon.block.tileentity;

import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.message.Message;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateTileEntitiesMessage implements Message {

    public static class TileEntityUpdate{
        public final int tileX;
        public final int tileY;
        public final float[] data;
        public final List<ItemBuffer> items;

        public TileEntityUpdate(TileEntity entity) {
            this.tileX = entity.getRootTile().getX();
            this.tileY = entity.getRootTile().getY();
            this.data = entity.getData();

            if(entity instanceof HasInventory){
                var inventory = (HasInventory) entity;
                items = Arrays.stream(inventory.getBuffers()).map(ItemBuffer::copy).collect(Collectors.toList());
            }else{
                items = null;
            }
        }
    }

    private final TileEntityUpdate[] updates;

    public UpdateTileEntitiesMessage(List<TileEntity> entities) {
        updates = new TileEntityUpdate[entities.size()];

        for(int i = 0; i < entities.size(); i++) {
            updates[i] = new TileEntityUpdate(entities.get(i));
        }
    }

    public TileEntityUpdate[] getUpdates() {
        return updates;
    }
}
