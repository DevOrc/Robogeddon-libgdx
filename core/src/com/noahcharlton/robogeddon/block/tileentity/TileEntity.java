package com.noahcharlton.robogeddon.block.tileentity;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.electricity.PowerGraph;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

public class TileEntity {

    protected final Tile rootTile;
    protected final World world;

    protected boolean dirty = false;

    public TileEntity(Tile rootTile) {
        this.rootTile = rootTile;
        this.world = rootTile.getWorld();
    }

    public void update(){}

    public void save(XmlWriter xml) {
        xml.dataElement("Data", getSaveData());

        if(this instanceof HasInventory){
            HasInventory.save((HasInventory) this, xml);
        }
    }

    public void load(Element xml) {
        loadSaveData(xml.getDataElement("Data"));

        if(this instanceof HasInventory){
            HasInventory.load((HasInventory) this, xml);
        }

        dirty = true;
    }

    @Side(Side.SERVER)
    protected float[] getSaveData(){
        return getData();
    }

    @Side(Side.SERVER)
    protected void loadSaveData(float[] data){
        receiveData(data);
    }

    @Side(Side.SERVER)
    public float[] getData(){
        return new float[]{};
    }

    public void receiveData(float[] data){

    }

    public static float[] combineFloatArrays(float[] array1, float[] array2){
        var result = new float[array1.length + array2.length];

        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);

        return result;
    }

    @Side(Side.BOTH)
    public void onCustomMessageReceived(CustomTileEntityMessage message){
        Log.warn("Unhandled custom tile entity message: " + message);
    }

    protected PowerGraph getTeamPowerGraph(){
        return world.getPowerForTeam(getRootTile().getChunk().getTeam());
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
