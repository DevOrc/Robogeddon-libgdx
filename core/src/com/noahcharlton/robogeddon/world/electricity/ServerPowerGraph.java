package com.noahcharlton.robogeddon.world.electricity;

import com.noahcharlton.robogeddon.block.tileentity.electricity.HasElectricity;
import com.noahcharlton.robogeddon.world.Tile;

import java.util.HashMap;
import java.util.Map;

public class ServerPowerGraph implements PowerGraph {

    private final HashMap<Tile, HasElectricity> connections = new HashMap<>();

    private float generatedPower;
    private float consumedPower;

    public void add(Tile tile, HasElectricity electricity) {
        if(!connections.containsKey(tile)){
            System.out.println("Added connection: " + tile);
            connections.put(tile, electricity);
        }
    }

    public void update() {
        generatedPower = 0;
        consumedPower = 0;

        connections.entrySet().removeIf(e -> !isConnectionValid(e));

        for(HasElectricity connection : connections.values()){
            generatedPower += connection.getPowerBuffer().getPowerGenerated();
            consumedPower += connection.getPowerBuffer().getPowerWanted();
        }
    }

    private boolean isConnectionValid(Map.Entry<Tile, HasElectricity> entry) {
        var tile = entry.getKey();
        var electricity = entry.getValue();
        return electricity.isConnectedToRelay() && tile.hasBlock() &&
                tile.getTileEntity() instanceof HasElectricity;
    }

    public float getGeneratedPower() {
        return generatedPower;
    }

    public float getConsumedPower() {
        return consumedPower;
    }
}
