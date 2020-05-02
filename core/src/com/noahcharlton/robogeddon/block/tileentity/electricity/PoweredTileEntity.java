package com.noahcharlton.robogeddon.block.tileentity.electricity;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.electricity.PowerGraph;

public class PoweredTileEntity extends TileEntity implements HasElectricity {

    private final ConsumerPowerBuffer buffer;

    private Tile relayTile;

    private boolean hasPower;

    public PoweredTileEntity(Tile rootTile, float usageRate) {
        super(rootTile);

        this.buffer = new ConsumerPowerBuffer(usageRate);
    }

    @Override
    public void update() {
        updateElectricity();
    }

    public void usePower() {
        if(world.isClient())
            throw new UnsupportedOperationException();

        boolean lastHasPower = hasPower;
        hasPower = buffer.usePower();

        if(lastHasPower != hasPower){
            dirty = true;
        }
    }

    @Override
    public float[] getData() {
        return new float[]{hasPower ? 1f : 0f};
    }

    @Override
    public void receiveData(float[] data) {
        hasPower = data[0] == 1f;
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
    public PowerBuffer getPowerBuffer() {
        return buffer;
    }

    @Override
    public PowerGraph getTeamPowerGraph() {
        return super.getTeamPowerGraph();
    }

    public boolean hasPower() {
        return hasPower;
    }
}
