package com.noahcharlton.robogeddon.block.fluid;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.fluid.FluidBuffer;
import com.noahcharlton.robogeddon.block.tileentity.fluid.HasFluid;
import com.noahcharlton.robogeddon.block.tileentity.fluid.SingleFluidBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.fluid.Fluids;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class WaterCollector extends Block implements HasTileEntity {

    private static final float WATER_PER_SECOND = 1f;

    public WaterCollector(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requirements = List.of(Items.iron.stack(35), Items.circuit.stack(1));
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                FloatUtils.asIntString(WATER_PER_SECOND)
        };
    }

    @Override
    public String getDisplayName() {
        return "Water Collector";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new WaterCollectorBlock(tile);
    }

    static class WaterCollectorBlock extends TileEntity implements HasFluid {

        private FluidBuffer fluidBuffer = new SingleFluidBuffer(Fluids.coldWater, 25f);

        @Side(Side.SERVER)
        private int waterTick;

        public WaterCollectorBlock(Tile rootTile) {
            super(rootTile);
        }

        @Override
        public void update() {
            if(world.isServer()){
                waterTick++;

                if(waterTick > 60){
                    waterTick = 0;
                    fluidBuffer.acceptFluid(WATER_PER_SECOND);
                    dirty = true;
                }

                outputFluids(fluidBuffer, this);
            }
        }

        @Override
        public FluidBuffer getInputBuffer() {
            return null;
        }

        @Override
        public FluidBuffer[] getFluidBuffers() {
            return new FluidBuffer[]{fluidBuffer};
        }

        @Override
        public void setFluidBuffers(FluidBuffer[] buffers) {
            fluidBuffer = buffers[0];
        }
    }
}
