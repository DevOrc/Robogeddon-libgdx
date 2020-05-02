package com.noahcharlton.robogeddon.block.tileentity.electricity;

public interface PowerBuffer {

    float getPowerWanted();

    float getPowerGenerated();

    void receivePower(float amount);

    default String[] getPowerBufferDetails(){
        return new String[0];
    }

}
