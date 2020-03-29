package com.noahcharlton.robogeddon.world.floor;

import com.noahcharlton.robogeddon.Core;

public class Floors {

    public static final Floor rock = new Floor("rock");
    public static final Floor dirt = new Floor("dirt");
    public static final Floor grass = new Floor("grass");
    public static final Floor sand = new Floor("sand");
    public static final Floor oil_rock = new Floor("oil_rock");

    public static void preInit(){
        Core.floors.register(rock);
        Core.floors.register(dirt);
        Core.floors.register(grass);
        Core.floors.register(sand);
        Core.floors.register(oil_rock);
    }
}
