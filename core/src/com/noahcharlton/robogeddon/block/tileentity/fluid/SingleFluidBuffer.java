package com.noahcharlton.robogeddon.block.tileentity.fluid;

import com.badlogic.gdx.math.MathUtils;
import com.noahcharlton.robogeddon.world.fluid.Fluid;

public class SingleFluidBuffer implements FluidBuffer {

    private final Fluid fluid;
    private final float capacity;
    private float amount;

    public SingleFluidBuffer(Fluid fluid, float capacity) {
        this.fluid = fluid;
        this.capacity = capacity;
    }

    @Override
    public void acceptFluid(float delta) {
        amount = MathUtils.clamp(delta + amount, 0, capacity);
    }

    @Override
    public void retrieveFluid(float delta) {
        amount = MathUtils.clamp(amount - delta, 0, capacity);
    }

    @Override
    public void setFluid(Fluid fluid) {
        throw new UnsupportedOperationException("Fluids cannot be changed!");
    }

    @Override
    public FluidBuffer copy() {
        var buffer = new SingleFluidBuffer(fluid, capacity);
        buffer.amount = this.amount;

        return buffer;
    }

    @Override
    public boolean isFull() {
        return amount >= capacity - .001;
    }

    @Override
    public Fluid getFluid() {
        return fluid;
    }

    @Override
    public float getAmount() {
        return amount;
    }

    @Override
    public float getCapacity() {
        return capacity;
    }
}
