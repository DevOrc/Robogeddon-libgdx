package com.noahcharlton.robogeddon;

import com.noahcharlton.robogeddon.asset.AssetManager;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.floor.Floor;
import com.noahcharlton.robogeddon.world.floor.Floors;

public class Core {

    public static final String VERSION = "0.1.0";
    public static final long UPDATE_RATE = 60;
    public static final int PORT = 14772;

    public static final Registry<EntityType> entities = new Registry<>();
    public static final Registry<Block> blocks = new Registry<>();
    public static final Registry<Floor> floors = new Registry<>();

    @Side(Side.CLIENT)
    public static AssetManager assets;
    /** Responsible for getting graphics details from the client for the core code */
    @Side(Side.CLIENT)
    public static Client client;


    @Side(Side.BOTH)
    public static void preInit(){
        Log.debug("PreInit Start");

        EntityType.preInit();
        Blocks.preInit();
        Floors.preInit();

        entities.setFinalized(true);
        blocks.setFinalized(true);
        floors.setFinalized(true);
        Log.debug("PreInit End");
    }

    @Side(Side.CLIENT)
    public static void init(){
        Log.debug("Init");
        assets = new AssetManager();

        entities.values().forEach(EntityType::initRenderer);
        blocks.values().forEach(Block::initRenderer);
        floors.values().forEach(Floor::init);
        Log.debug("Init End");
    }
}
