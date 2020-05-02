package com.noahcharlton.robogeddon.block.tileentity.electricity;

public class EmptyPowerBuffer implements PowerBuffer{

    @Override
    public float getPowerWanted() {
        return 0;
    }

    @Override
    public float getPowerGenerated() {
        return 0;
    }
}
