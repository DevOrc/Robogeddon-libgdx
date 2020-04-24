package com.noahcharlton.robogeddon.world.item;

import com.noahcharlton.robogeddon.Core;

public class Items {

    public static final Item rock = new Item("rock", "Rock");
    public static final Item iron = new Item("iron", "Iron");

    public static void preInit(){
        Core.items.register(rock);
        Core.items.register(iron);
    }
}
