package com.noahcharlton.robogeddon.world.floor;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.world.item.Items;

public class Floors {

    public static final Floor rock = new Floor("rock", "Rock");
    public static final Floor dirt1 = new Floor("dirt1", "Dirt");
    public static final Floor dirt2 = new Floor("dirt2", "Dirt");
    public static final Floor rockyDirt = new Floor("rockyDirt", "Rocky Dirt");
    public static final Floor grass = new Floor("grass", "Grass");
    public static final Floor sand = new Floor("sand", "Sand");

    public static final Floor ironRock = new OreFloor("iron", "Iron Ore", Items.iron);

    public static final Floor[] dirtTypes = new Floor[]{dirt1, rockyDirt, dirt2};

    public static void preInit(){
        Core.floors.register(rock);
        Core.floors.register(dirt1);
        Core.floors.register(rockyDirt);
        Core.floors.register(dirt2);
        Core.floors.register(grass);
        Core.floors.register(sand);
        Core.floors.register(ironRock);
    }

    public static boolean isDirt(Floor floor){
        for(Floor dirtType : dirtTypes){
            if(floor == dirtType){
                return true;
            }
        }

        return false;
    }
}
