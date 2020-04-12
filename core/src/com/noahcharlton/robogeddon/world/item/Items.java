package com.noahcharlton.robogeddon.world.item;

import com.noahcharlton.robogeddon.Core;

public class Items {

    public static final Item rock = new Item("rock", "Rock");
    public static final Item ruby = new Item("ruby", "Ruby");
    public static final Item sapphire = new Item("sapphire", "Sapphire");

    public static void preInit(){
        Core.items.register(rock);
        Core.items.register(ruby);
        Core.items.register(sapphire);
    }
}
