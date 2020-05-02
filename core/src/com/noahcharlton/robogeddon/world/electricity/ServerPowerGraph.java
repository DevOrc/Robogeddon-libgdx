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
            connections.put(tile, electricity);
        }
    }

    public void update() {
        generatedPower = 0;
        consumedPower = 0;

        int consumerCount = 0;

        connections.entrySet().removeIf(e -> !isConnectionValid(e));

        for(HasElectricity connection : connections.values()){
            generatedPower += connection.getPowerBuffer().getPowerGenerated();
            var consumedPower = connection.getPowerBuffer().getPowerWanted();

            consumerCount += consumedPower > 0 ? 1 : 0;

            this.consumedPower += consumedPower;
        }

        //pull from batteries if need be
        distributePower(consumerCount);
    }

    private void distributePower(int consumerCount) {
        if(generatedPower >= consumedPower){
            for(HasElectricity connection : connections.values()){
                var powerWanted = connection.getPowerBuffer().getPowerWanted();
                connection.getPowerBuffer().receivePower(powerWanted);
            }

            //Send extra to batteries
        }else{
            float powerPer = generatedPower / consumerCount;

            for(HasElectricity connection : connections.values()){
                var wantsPower = connection.getPowerBuffer().getPowerWanted() > 0;

                if(wantsPower){
                    connection.getPowerBuffer().receivePower(powerPer);
                }
            }
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
