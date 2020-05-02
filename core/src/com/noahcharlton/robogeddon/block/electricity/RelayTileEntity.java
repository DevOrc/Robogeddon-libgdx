package com.noahcharlton.robogeddon.block.electricity;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.EmptyPowerBuffer;
import com.noahcharlton.robogeddon.block.tileentity.electricity.HasElectricity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.PowerBuffer;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.electricity.PowerGraph;

public class RelayTileEntity extends TileEntity implements HasElectricity {

    private static final PowerBuffer emptyBuffer = new EmptyPowerBuffer();

    public RelayTileEntity(Tile rootTile) {
        super(rootTile);
    }

    @Override
    public PowerBuffer getPowerBuffer() {
        return emptyBuffer;
    }

    @Override
    public Tile getRelayTile() {
        return getRootTile();
    }

    @Override
    public PowerGraph getTeamPowerGraph() {
        return super.getTeamPowerGraph();
    }

    @Override
    public void setRelayTile(Tile tile) {
        throw new IllegalStateException("Relays don't need to connect to other relays!");
    }

}
