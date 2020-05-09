package com.noahcharlton.robogeddon.block.crafting;

import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;

public class CrafterTileEntity extends TileEntity implements HasInventory {

    private final int craftTime;
    private ItemBuffer[] inputBuffers;
    private ItemBuffer outputBuffer;

    private int tick;

    public CrafterTileEntity(Tile rootTile, int craftTime, ItemBuffer outputBuffer, ItemBuffer... inputBuffers) {
        super(rootTile);

        this.craftTime = craftTime;
        this.inputBuffers = inputBuffers;
        this.outputBuffer = outputBuffer;
    }

    @Override
    public void update() {
        if(!canBeCrating())
            return;

        tick++;

        if(world.isServer() && tick > craftTime && !outputBuffer.isFull()){
            outputBuffer.acceptItem(outputBuffer.currentItem());

            for(ItemBuffer inputBuffer: inputBuffers){
                inputBuffer.retrieveItem();
            }

            tick = 0;
            dirty = true;
        }
    }

    protected boolean canBeCrating() {
        for(ItemBuffer inputBuffer: inputBuffers){
            if(inputBuffer.getAmount() <= 0)
                return false;
        }

        return true;
    }

    @Override
    public boolean acceptItem(Item item, Direction from) {
        for(ItemBuffer inputBuffer : inputBuffers){
            if(inputBuffer.acceptItem(item)){
                dirty = true;
                return true;
            }
        }

        return false;
    }

    @Override
    public void receiveData(float[] data) {
        tick = (int) data[0];
    }

    @Override
    public float[] getData() {
        return new float[]{tick};
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {
        outputBuffer = buffers[0];

        System.arraycopy(buffers, 1, inputBuffers, 0, buffers.length - 1);
    }

    @Override
    public Item retrieveItem(boolean simulate, Direction to) {
        if(simulate){
            return outputBuffer.getAmount() > 0 ? outputBuffer.currentItem() : null;
        }

        dirty = true;
        return outputBuffer.retrieveItem();
    }

    @Override
    public ItemBuffer[] getBuffers() {
        var buffers = new ItemBuffer[inputBuffers.length + 1];
        buffers[0] = outputBuffer;

        System.arraycopy(inputBuffers, 0, buffers, 1, inputBuffers.length);

        return buffers;
    }

    public int getTick() {
        return tick;
    }
}
