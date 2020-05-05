package com.noahcharlton.robogeddon.world.item;

import com.noahcharlton.robogeddon.Core;

public class Items {

    public static final Item rock = new Item("rock", "Rock");
    public static final Item iron = new Item("iron", "Iron");
    public static final Item coal = new Item("coal", "Coal");
    public static final Item sand = new Item("sand", "Sand");
    public static final Item ironBullet = new Item("bullet_iron", "Iron Bullet");
    public static final Item circuit = new Item("circuit", "Circuit");

    public static void preInit(){
        Core.items.registerAll(rock, iron, coal, sand, ironBullet, circuit);
    }
}
