package com.noahcharlton.robogeddon;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.util.Side;

public class Core {

    public static final String VERSION = "0.1.0";
    public static final long UPDATE_RATE = 60;
    public static final int PORT = 14772;

    public static final Registry<EntityType> entities = new Registry<>();
    public static final Registry<Block> blocks = new Registry<>();

    @Side(Side.BOTH)
    public static void preInit(){
        Log.debug("PreInit Start");

        EntityType.preInit();
        Blocks.preInit();

        entities.setFinalized(true);
        blocks.setFinalized(true);
        Log.debug("PreInit End");
    }

    @Side(Side.CLIENT)
    public static void init(){
        Log.debug("Init");

        EntityType.init();
        Blocks.init();
        Log.debug("Init End");
    }
}
