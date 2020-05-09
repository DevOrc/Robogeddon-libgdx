package com.noahcharlton.robogeddon.block.tileentity.fluid;

import com.badlogic.gdx.math.MathUtils;
import com.noahcharlton.robogeddon.world.fluid.Fluid;

public class GenericFluidBuffer implements FluidBuffer {

    private final float capacity;
    private Fluid fluid;
    private float amount;

    public GenericFluidBuffer(float capacity) {
        this.capacity = capacity;
    }

    @Override
    public void acceptFluid(float delta) {
        amount = MathUtils.clamp(amount + delta, 0, capacity);
    }

    @Override
    public void retrieveFluid(float delta) {
        amount = MathUtils.clamp(amount - delta, 0, capacity);

        if(amount <= 0)
            fluid = null;
    }

    @Override
    public void setFluid(Fluid fluid) {
        this.fluid = fluid;
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

    @Override
    public FluidBuffer copy() {
        var buffer = new GenericFluidBuffer(capacity);
        buffer.fluid = this.fluid;
        buffer.amount = this.amount;

        return buffer;
    }

    @Override
    public boolean isFull() {
        return amount >= capacity - .001;
    }
}
