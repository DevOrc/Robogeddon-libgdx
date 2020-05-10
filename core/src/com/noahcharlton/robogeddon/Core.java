package com.noahcharlton.robogeddon;

import com.noahcharlton.robogeddon.asset.AssetManager;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockGroup;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.fluid.FluidBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.command.Command;
import com.noahcharlton.robogeddon.command.Commands;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.message.InterfaceSerializer;
import com.noahcharlton.robogeddon.message.MessageSerializer;
import com.noahcharlton.robogeddon.message.RegistrySerializer;
import com.noahcharlton.robogeddon.settings.Setting;
import com.noahcharlton.robogeddon.settings.SettingsIO;
import com.noahcharlton.robogeddon.util.MiscTextures;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.world.floor.Floor;
import com.noahcharlton.robogeddon.world.floor.Floors;
import com.noahcharlton.robogeddon.world.fluid.Fluid;
import com.noahcharlton.robogeddon.world.fluid.Fluids;
import com.noahcharlton.robogeddon.world.gen.Biome;
import com.noahcharlton.robogeddon.world.gen.Biomes;
import com.noahcharlton.robogeddon.world.io.WorldIOHandler;
import com.noahcharlton.robogeddon.world.item.Item;
import com.noahcharlton.robogeddon.world.item.Items;

public class Core {

    public static final String VERSION = "0.3.0";
    public static final String VERSION_TYPE = "alpha";

    public static final long UPDATE_RATE = 60;
    public static final int PORT = 14772;

    public static final Registry<EntityType> entities = new Registry<>();
    public static final Registry<Block> blocks = new Registry<>();
    public static final Registry<Floor> floors = new Registry<>();
    public static final Registry<Item> items = new Registry<>();
    public static final Registry<Fluid> fluids = new Registry<>();
    public static final OrderedRegistry<Setting> settings = new OrderedRegistry<>();
    public static final Registry<Biome> biomes = new Registry<>();

    @Side(Side.CLIENT)
    public static final Registry<BlockGroup> blockGroups = new Registry<>();
    @Side(Side.SERVER)
    public static final Registry<WorldIOHandler> saveGameHandlers = new Registry<>();
    @Side(Side.SERVER)
    public static final Registry<Command> commands = new Registry<>();

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
        Fluids.preInit();
        WorldIOHandler.preInit();
        Biomes.preInit();
        Commands.preInit();
        createMessageSerializers();

        entities.setFinalized(true);
        blocks.setFinalized(true);
        floors.setFinalized(true);
        items.setFinalized(true);
        fluids.setFinalized(true);
        saveGameHandlers.setFinalized(true);
        settings.setFinalized(true);
        commands.setFinalized(true);
        SettingsIO.load();
        MessageSerializer.finalizeSerializer();


        //Throw an exception if any block ID starts with multi, because those are reserved for multiblocks
        blocks.keys().stream().filter(id -> id.startsWith("multi")).findAny().ifPresent(x -> {
            throw new RuntimeException("Cannot have any block IDs that start with multi!");
        });
        Log.debug("PreInit End");
    }

    private static void createMessageSerializers() {
        MessageSerializer.registerType(ItemBuffer.class, InterfaceSerializer.create());
        MessageSerializer.registerType(FluidBuffer.class, InterfaceSerializer.create());
        MessageSerializer.registerType(Fluid.class, new RegistrySerializer<>(Core.fluids));
        MessageSerializer.registerType(Item.class, new RegistrySerializer<>(Core.items));
    }

    @Side(Side.CLIENT)
    public static void init(){
        Log.debug("Init");
        assets = new AssetManager();

        Blocks.init();
        MiscTextures.init();
        blockGroups.setFinalized(true);

        entities.values().forEach(EntityType::initRenderer);
        blocks.values().forEach(Block::initRenderer);
        floors.values().forEach(Floor::init);
        items.values().forEach(Item::init);
        Log.debug("Init End");
    }
}
