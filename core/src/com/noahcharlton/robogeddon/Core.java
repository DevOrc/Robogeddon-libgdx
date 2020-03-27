package com.noahcharlton.robogeddon;

import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.util.Side;

public class Core {

    public static final String VERSION = "0.1.0";
    public static final Registry<EntityType> entities = new Registry<>();

    @Side(Side.BOTH)
    public static void preInit(){
        Log.debug("PreInit");

        EntityType.preInit();

        entities.setFinalized(true);
    }

    @Side(Side.CLIENT)
    public static void init(){
        Log.debug("PreInit");

        EntityType.init();
    }
}
