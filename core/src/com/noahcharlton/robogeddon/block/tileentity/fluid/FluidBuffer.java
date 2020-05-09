package com.noahcharlton.robogeddon.block.tileentity.fluid;

import com.noahcharlton.robogeddon.world.fluid.Fluid;

public interface FluidBuffer {

    void acceptFluid(float amount);

    void retrieveFluid(float amount);

    void setFluid(Fluid fluid);

    Fluid getFluid();

    float getAmount();

    float getCapacity();

    FluidBuffer copy();

    boolean isFull();
}
