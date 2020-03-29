package com.noahcharlton.robogeddon.world.floor;

import com.noahcharlton.robogeddon.Core;

public class Floors {

    public static final Floor rock = new Floor("rock");
    public static final Floor dirt = new Floor("dirt");
    public static final Floor grass = new Floor("grass");

    public static void preInit(){
        Core.floors.register(rock);
        Core.floors.register(dirt);
        Core.floors.register(grass);
    }
}
