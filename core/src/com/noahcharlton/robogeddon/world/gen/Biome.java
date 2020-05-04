package com.noahcharlton.robogeddon.world.gen;

import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.floor.Floors;

public class Biome implements HasID {

    private final String name;
    private final double lowerVal;
    private final double higherVal;

    Biome(String name, double lowerVal, double higherVal) {
        this.name = name;
        this.lowerVal = lowerVal;
        this.higherVal = higherVal;
    }

    public void gen(Tile tile, double noiseValue){
        tile.setFloor(Floors.dirt1, false);
    }

    public boolean isInRange(double val){
        return val > lowerVal && val < higherVal;
    }

    @Override
    public String getTypeID() {
        return name;
    }
}
