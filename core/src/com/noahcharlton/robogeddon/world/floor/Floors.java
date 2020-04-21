package com.noahcharlton.robogeddon.world.floor;

import com.noahcharlton.robogeddon.Core;

import java.util.Random;

public class Floors {

    public static final Floor rock = new Floor("rock");
    public static final Floor dirt1 = new Floor("dirt1");
    public static final Floor dirt2 = new Floor("dirt2");
    public static final Floor dirt3 = new Floor("dirt3");
    public static final Floor grass = new Floor("grass");
    public static final Floor sand = new Floor("sand");
    public static final Floor oil_rock = new Floor("oil_rock");

    public static final Floor[] dirtTypes = new Floor[]{dirt1, dirt2, dirt3};

    public static void preInit(){
        Core.floors.register(rock);
        Core.floors.register(dirt1);
        Core.floors.register(dirt2);
        Core.floors.register(dirt3);
        Core.floors.register(grass);
        Core.floors.register(sand);
        Core.floors.register(oil_rock);
    }

    public static Floor randomDirt(){
        return dirtTypes[new Random().nextInt(dirtTypes.length)];
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
