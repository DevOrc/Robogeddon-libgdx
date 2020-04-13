package com.noahcharlton.robogeddon.block.tileentity;

import com.noahcharlton.robogeddon.Log;
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
        return new float[]{};
    }

    @Side(Side.CLIENT)
    public void receiveData(float[] data){

    }

    @Side(Side.BOTH)
    public void onCustomMessageReceived(CustomTileEntityMessage message){
        Log.warn("Unhandled custom tile entity message: " + message);
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
