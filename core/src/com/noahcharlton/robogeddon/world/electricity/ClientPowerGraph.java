package com.noahcharlton.robogeddon.world.electricity;

import com.noahcharlton.robogeddon.util.Side;

public class ClientPowerGraph implements PowerGraph {

    private float consumedPower;
    private float generatedPower;

    @Side(Side.SERVER)
    ClientPowerGraph(ServerPowerGraph serverGraph) {
        consumedPower = serverGraph.getConsumedPower();
        generatedPower = serverGraph.getGeneratedPower();
    }

    public float getConsumedPower() {
        return consumedPower;
    }

    public float getGeneratedPower() {
        return generatedPower;
    }
}
