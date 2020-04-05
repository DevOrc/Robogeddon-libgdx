package com.noahcharlton.robogeddon.block.tileentity;

import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.World;

public class TileEntity {

    protected final Tile rootTile;
    protected final World world;

    protected boolean dirty = false;

    public TileEntity(Tile rootTile) {
        this.rootTile = rootTile;
        this.world = rootTile.getWorld();
    }

    public void update(){}

    @Side(Side.SERVER)
    public float[] getData(){
        throw new IllegalStateException("getData() must be extended if a tile entity is marked dirty.");
    }

    @Side(Side.CLIENT)
    public void receiveData(float[] data){
        throw new IllegalStateException("receiveData() must be extended if a tile entity is marked dirty.");
    }

    public void clean(){
        dirty = false;
    }

    public boolean isDirty() {
        return dirty;
    }

    public Tile getRootTile() {
        return rootTile;
    }
}
