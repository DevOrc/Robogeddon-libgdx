package com.noahcharlton.robogeddon.world.fluid;

import com.badlogic.gdx.graphics.Color;
import com.noahcharlton.robogeddon.Core;

public class Fluids {

    public static final Fluid warmWater = new Fluid("warm_water", "Warm Water", new Color(0x00ABFFff));
    public static final Fluid coldWater = new Fluid("cold_water", "Cold Water", new Color(0x0066CCff));

    public static void preInit() {
        Core.fluids.registerAll(coldWater, warmWater);
    }
}
