package com.noahcharlton.robogeddon.block.tileentity.electricity;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.electricity.PowerGraph;

public class GeneratorTileEntity extends TileEntity implements HasElectricity, PowerBuffer {

    private final float powerPerTick;

    private Tile relayTile;

    protected boolean generating = true;

    public GeneratorTileEntity(Tile rootTile, float powerPerTick) {
        super(rootTile);

        this.powerPerTick = powerPerTick;
    }

    @Override
    public void update() {
        this.updateElectricity();
    }

    @Override
    public PowerBuffer getPowerBuffer() {
        return this;
    }

    @Override
    public String[] getPowerBufferDetails() {
        return new String[] {"Generating " + getPowerGenerated() + " EU"};
    }

    @Override
    public void setRelayTile(Tile tile) {
        this.relayTile = tile;
    }

    @Override
    public Tile getRelayTile() {
        return relayTile;
    }

    @Override
    public PowerGraph getTeamPowerGraph() {
        return super.getTeamPowerGraph();
    }

    @Override
    public float getPowerWanted() {
        return 0;
    }

    @Override
    public float getPowerGenerated() {
        return generating ? powerPerTick : 0;
    }

    @Override
    public void receivePower(float amount) {

    }

    public boolean isGenerating() {
        return generating;
    }
}
