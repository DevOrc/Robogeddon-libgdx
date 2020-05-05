package com.noahcharlton.robogeddon.world.floor;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.world.item.Items;

public class Floors {

    public static final Floor dirt1 = new Floor("dirt1", "Dirt");
    public static final Floor dirt2 = new Floor("dirt2", "Dirt");
    public static final Floor grass = new Floor("grass", "Grass");

    public static final Floor sand = new MineableFloor("sand", "Sand", Items.sand);
    public static final Floor sand2 = new MineableFloor("sand2", "Sand", Items.sand);
    public static final Floor rockySand = new MineableFloor("rockySand", "Rocky Sand", Items.rock);
    public static final Floor rock = new MineableFloor("rock", "Rock", Items.rock);
    public static final Floor rockyDirt = new MineableFloor("rockyDirt", "Rocky Dirt", Items.rock);
    public static final Floor ironOre = new MineableFloor("iron", "Iron Ore", Items.iron);
    public static final Floor coalOre = new MineableFloor("coal", "Coal Ore", Items.coal);

    public static final Floor[] dirtTypes = new Floor[]{dirt1, rockyDirt, dirt2};

    public static void preInit(){
        Core.floors.register(rock);
        Core.floors.register(dirt1);
        Core.floors.register(rockyDirt);
        Core.floors.register(dirt2);
        Core.floors.register(grass);
        Core.floors.register(sand);
        Core.floors.register(ironOre);
        Core.floors.register(coalOre);
        Core.floors.register(rockySand);
        Core.floors.register(sand2);
    }
}
