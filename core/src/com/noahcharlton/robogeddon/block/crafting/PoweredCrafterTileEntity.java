package com.noahcharlton.robogeddon.block.crafting;

import com.noahcharlton.robogeddon.block.tileentity.electricity.ConsumerPowerBuffer;
import com.noahcharlton.robogeddon.block.tileentity.electricity.HasElectricity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.PowerBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.electricity.PowerGraph;

public class PoweredCrafterTileEntity extends CrafterTileEntity implements HasElectricity {

    private final ConsumerPowerBuffer buffer;
    private Tile relayTile;

    private boolean hasPower;

    public PoweredCrafterTileEntity(Tile rootTile, float powerPerTick,
                                    int craftTime, ItemBuffer outputBuffer, ItemBuffer... inputBuffers) {
        super(rootTile, craftTime, outputBuffer, inputBuffers);

        this.buffer = new ConsumerPowerBuffer(powerPerTick);
    }

    @Override
    public void update() {
        this.updateElectricity();

        if(world.isServer() && this.canBeCrating()){
            boolean lastHasPower = hasPower;
            hasPower = buffer.usePower();

            if(lastHasPower != hasPower){
                dirty = true;
            }
        }

        if(hasPower)
            super.update();
    }

    @Override
    public float[] getData() {
        return combineFloatArrays(super.getData(), new float[]{hasPower ? 1f: 0f});
    }

    @Override
    public void receiveData(float[] data) {
        super.receiveData(data);

        hasPower = data[data.length - 1] == 1f;
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
}
