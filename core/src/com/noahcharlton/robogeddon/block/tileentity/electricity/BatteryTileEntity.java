package com.noahcharlton.robogeddon.block.tileentity.electricity;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.electricity.PowerGraph;

public class BatteryTileEntity extends TileEntity implements HasElectricity {

    private final BatteryPowerBuffer buffer;
    private Tile relayTile;

    private int lastUpdateTick;

    public BatteryTileEntity(Tile rootTile, float capacity) {
        super(rootTile);

        this.buffer = new BatteryPowerBuffer(capacity);
    }

    @Override
    public void update() {
        updateElectricity();

        if(world.isClient())
            return;

        lastUpdateTick++;

        if(lastUpdateTick > 20 && buffer.isDirty()){
            dirty = true;
            lastUpdateTick = 0;
            buffer.setDirty(false);
        }
    }

    @Override
    public void receiveData(float[] data) {
        if(data.length > 0)
            buffer.setStored(data[0]);
    }

    @Override
    public float[] getData() {
        return new float[]{buffer.getStored()};
    }

    @Override
    public void setRelayTile(Tile tile) {
        relayTile = tile;
    }

    @Override
    public Tile getRelayTile() {
        return relayTile;
    }

    @Override
    public BatteryPowerBuffer getPowerBuffer() {
        return buffer;
    }

    @Override
    public PowerGraph getTeamPowerGraph() {
        return super.getTeamPowerGraph();
    }
}
