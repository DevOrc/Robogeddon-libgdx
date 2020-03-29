package com.noahcharlton.robogeddon.world.item;

import com.noahcharlton.robogeddon.Core;

public class Items {

    public static final Item rock = new Item("rock");
    public static final Item ruby = new Item("ruby");
    public static final Item sapphire = new Item("sapphire");

    public static void preInit(){
        Core.items.register(rock);
        Core.items.register(ruby);
        Core.items.register(sapphire);
    }
}
