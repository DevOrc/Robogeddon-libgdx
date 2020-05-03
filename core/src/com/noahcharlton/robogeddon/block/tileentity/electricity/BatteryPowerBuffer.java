package com.noahcharlton.robogeddon.block.tileentity.electricity;

public class BatteryPowerBuffer implements PowerBuffer {

    private final float capacity;
    private float stored;

    private boolean dirty;

    public BatteryPowerBuffer(float capacity) {
        this.capacity = capacity;
    }

    @Override
    public float getPowerWanted() {
        return 0;
    }

    @Override
    public float getPowerGenerated() {
        return 0;
    }

    @Override
    public void receivePower(float amount) {
        float lastStored = stored;
        stored = Math.min(capacity, amount + stored);

        if(lastStored != stored){
            dirty = true;
        }
    }

    @Override
    public String[] getPowerBufferDetails() {
        return new String[]{
                String.format("Battery: %2.0f / %2.0f", stored, capacity)
        };
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public float getStored() {
        return stored;
    }

    public void setStored(float stored) {
        dirty = true;

        this.stored = Math.min(stored, capacity);
    }
}
