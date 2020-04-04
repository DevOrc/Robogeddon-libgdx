package com.noahcharlton.robogeddon;

import com.noahcharlton.robogeddon.asset.AssetManager;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.floor.Floor;
import com.noahcharlton.robogeddon.world.floor.Floors;
import com.noahcharlton.robogeddon.world.item.Item;
import com.noahcharlton.robogeddon.world.item.Items;

public class Core {

    public static final String VERSION = "0.2.0";
    public static final String VERSION_TYPE = "alpha";

    public static final long UPDATE_RATE = 60;
    public static final int PORT = 14772;

    public static final Registry<EntityType> entities = new Registry<>();
    public static final Registry<Block> blocks = new Registry<>();
    public static final Registry<Floor> floors = new Registry<>();
    public static final Registry<Item> items = new Registry<>();

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
        Items.preInit();

        entities.setFinalized(true);
        blocks.setFinalized(true);
        floors.setFinalized(true);
        items.setFinalized(true);

        //Throw an exception if any block ID starts with multi, because those are reserved for multiblocks
        blocks.keys().stream().filter(id -> id.startsWith("multi")).findAny().ifPresent(x -> {
            throw new RuntimeException("Cannot have any block IDs that start with multi!");
        });
        Log.debug("PreInit End");
    }

    @Side(Side.CLIENT)
    public static void init(){
        Log.debug("Init");
        assets = new AssetManager();

        entities.values().forEach(EntityType::initRenderer);
        blocks.values().forEach(Block::initRenderer);
        floors.values().forEach(Floor::init);
        items.values().forEach(Item::init);
        Log.debug("Init End");
    }
}
