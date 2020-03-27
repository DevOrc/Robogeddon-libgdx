package com.noahcharlton.robogeddon;

import com.noahcharlton.robogeddon.entity.EntityType;

public class Core {

    public static final String VERSION = "0.1.0";

    public static final Registry<EntityType> entities = new Registry<>();

    public static void preInit(){
        Log.debug("PreInit");

        EntityType.preInit();

        entities.setFinalized(true);
    }
}
