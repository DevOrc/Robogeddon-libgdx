package com.noahcharlton.robogeddon.block.duct;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.fluid.FluidBuffer;
import com.noahcharlton.robogeddon.block.tileentity.fluid.GenericFluidBuffer;
import com.noahcharlton.robogeddon.block.tileentity.fluid.HasFluid;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.world.Tile;

public class FluiductTileEntity extends TileEntity implements HasFluid {

    private FluidBuffer buffer = new GenericFluidBuffer(5);

    private boolean connectedNorth;
    private boolean connectedEast;
    private boolean connectedSouth;
    private boolean connectedWest;

    public FluiductTileEntity(Tile rootTile) {
        super(rootTile);
    }

    @Override
    public void update() {
        if(this.connectedNorth != canConnectTo(Direction.NORTH)){
            this.connectedNorth = !this.connectedNorth;
            dirty = true;
        }
        if(this.connectedEast != canConnectTo(Direction.EAST)){
            this.connectedEast = !this.connectedEast;
            dirty = true;
        }
        if(this.connectedSouth != canConnectTo(Direction.SOUTH)){
            this.connectedSouth = !this.connectedSouth;
            dirty = true;
        }
        if(this.connectedWest != canConnectTo(Direction.WEST)){
            this.connectedWest = !this.connectedWest;
            dirty = true;
        }

        if(world.isServer()){
            outputFluids(buffer, this);
        }
    }

    private boolean canConnectTo(Direction direction) {
        var tile = getRootTile().getNeighbor(direction);

        if(tile == null || tile.getTileEntity() == null)
            return false;

        return tile.getTileEntity() instanceof HasFluid;
    }

    @Override
    public FluidBuffer getInputBuffer() {
        return buffer;
    }

    @Override
    public FluidBuffer[] getFluidBuffers() {
        return new FluidBuffer[]{buffer};
    }

    @Override
    public void setFluidBuffers(FluidBuffer[] buffers) {
        this.buffer = buffers[0];
    }

    @Override
    public float[] getData() {
        return new float[]{
                toFloat(connectedNorth),
                toFloat(connectedEast),
                toFloat(connectedSouth),
                toFloat(connectedWest)
        };
    }

    @Override
    public void receiveData(float[] data) {
        connectedNorth = toBoolean(data[0]);
        connectedEast = toBoolean(data[1]);
        connectedSouth = toBoolean(data[2]);
        connectedWest = toBoolean(data[3]);
    }

    private float toFloat(boolean val){
        return val ? 1f : 0f;
    }

    private boolean toBoolean(float val){
        return val == 1f;
    }

    public boolean isConnectedNorth() {
        return connectedNorth;
    }

    public boolean isConnectedEast() {
        return connectedEast;
    }

    public boolean isConnectedSouth() {
        return connectedSouth;
    }

    public boolean isConnectedWest() {
        return connectedWest;
    }
}
