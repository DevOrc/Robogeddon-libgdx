package com.noahcharlton.robogeddon.block.tileentity.electricity;

public class ConsumerPowerBuffer implements PowerBuffer {

    /**
     * How many times bigger is the storage of this buffer
     * compared to the usage rate.
     */
    private static final float BUFFER_SCALE = 4f;

    private final float usageRate;

    private float stored;

    public ConsumerPowerBuffer(float usageRate) {
        this.usageRate = usageRate;
    }

    @Override
    public void receivePower(float amount) {
        stored = Math.min(usageRate * BUFFER_SCALE, amount + stored);
    }

    @Override
    public float getPowerWanted() {
        return usageRate;
    }

    @Override
    public float getPowerGenerated() {
        return 0;
    }

    public float getStored() {
        return stored;
    }

    public float getUsageRate() {
        return usageRate;
    }

    public boolean usePower() {
        if(stored >= usageRate){
            stored -= usageRate;
            return true;
        }

        return false;
    }
}
