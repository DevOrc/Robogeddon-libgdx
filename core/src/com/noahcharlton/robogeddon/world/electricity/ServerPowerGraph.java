package com.noahcharlton.robogeddon.world.electricity;

import com.noahcharlton.robogeddon.block.tileentity.electricity.BatteryTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.HasElectricity;
import com.noahcharlton.robogeddon.world.Tile;

import java.util.HashMap;
import java.util.Map;

public class ServerPowerGraph implements PowerGraph {

    private final HashMap<Tile, HasElectricity> connections = new HashMap<>();
    private final HashMap<Tile, BatteryTileEntity> batteries = new HashMap<>();

    private float generatedPower;
    private float consumedPower;

    public void add(Tile tile, HasElectricity electricity) {
        if(!connections.containsKey(tile)){
            connections.put(tile, electricity);
        }

        if(!batteries.containsKey(tile) && electricity instanceof BatteryTileEntity){
            batteries.put(tile, (BatteryTileEntity) electricity);
        }
    }

    public void update() {
        generatedPower = 0;
        consumedPower = 0;

        int consumerCount = 0;

        connections.entrySet().removeIf(e -> !isConnectionValid(e));
        batteries.entrySet().removeIf(e -> !isConnectionValid(e));

        for(HasElectricity connection : connections.values()){
            generatedPower += connection.getPowerBuffer().getPowerGenerated();
            var consumedPower = connection.getPowerBuffer().getPowerWanted();

            consumerCount += consumedPower > 0 ? 1 : 0;

            this.consumedPower += consumedPower;
        }

        if(generatedPower < consumedPower){
            drawPowerFromBatteries();
        }

        distributePower(consumerCount);
    }

    private void distributePower(int consumerCount) {
        if(generatedPower >= consumedPower){
            for(HasElectricity connection : connections.values()){
                var powerWanted = connection.getPowerBuffer().getPowerWanted();
                connection.getPowerBuffer().receivePower(powerWanted);
            }

            if(generatedPower != consumedPower)
                storeExtraPower();
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

    private void drawPowerFromBatteries() {
        float powerNeeded = consumedPower - generatedPower;

        if(powerNeeded < 0)
            throw new IllegalStateException("Cannot draw from battery if there is a surplus of power!");

        for(BatteryTileEntity battery : batteries.values()){
            float stored = battery.getPowerBuffer().getStored();
            float withdrawn = Math.min(stored, powerNeeded);

            battery.getPowerBuffer().setStored(stored - withdrawn);
            powerNeeded -= withdrawn;
            generatedPower += withdrawn;

            if(powerNeeded == 0){
                return;
            } else if(powerNeeded < 0){
                throw new RuntimeException("Pulled more power than was needed??");
            }
        }

    }

    private void storeExtraPower() {
        float extraPower = generatedPower - consumedPower;

        if(extraPower <= 0){
            throw new IllegalStateException("Must have generated power to store it!");
        }

        float powerPerBattery = extraPower / batteries.size();

        batteries.values().forEach(b -> b.getPowerBuffer().receivePower(powerPerBattery));
    }

    private <E extends HasElectricity> boolean isConnectionValid(Map.Entry<Tile, E> entry) {
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
