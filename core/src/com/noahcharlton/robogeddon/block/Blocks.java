package com.noahcharlton.robogeddon.block;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.beacon.BeaconBlock;
import com.noahcharlton.robogeddon.block.crafting.CircuitFactory;
import com.noahcharlton.robogeddon.block.crafting.MetalFormerBlock;
import com.noahcharlton.robogeddon.block.duct.ItemDuct;
import com.noahcharlton.robogeddon.block.electricity.*;
import com.noahcharlton.robogeddon.block.miner.MinerBlock;
import com.noahcharlton.robogeddon.block.portal.InventoryPortalBlock;
import com.noahcharlton.robogeddon.block.portal.UnloaderBlock;
import com.noahcharlton.robogeddon.block.turret.TurretBlock;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.team.Team;

public class Blocks{

    private static final String itemDuctID = "item_duct";

    public static final Block testBlock = new TestBlock("test_block");
    public static final Block turretBlock = new TurretBlock("turret");
    public static final Block blueBeacon = new BeaconBlock("beacon", Team.BLUE);
    public static final Block redBeacon = new BeaconBlock("beacon", Team.RED);
    public static final Block minerBlock = new MinerBlock("miner");
    public static final Block itemDuctNorth = new ItemDuct(itemDuctID, Direction.NORTH);
    public static final Block itemDuctSouth = new ItemDuct(itemDuctID, Direction.SOUTH);
    public static final Block itemDuctEast = new ItemDuct(itemDuctID, Direction.EAST);
    public static final Block itemDuctWest = new ItemDuct(itemDuctID, Direction.WEST);
    public static final Block inventoryPortal = new InventoryPortalBlock("inventory_portal");
    public static final Block unloaderBlock = new UnloaderBlock("unloader");
    public static final Block wall = new WallBlock("wall");
    public static final Block metalFormer = new MetalFormerBlock("metal_former");
    public static final Block relayBlock = new RelayBlock("relay");
    public static final Block solarPanel = new SolarPanelBlock("solar_panel");
    public static final Block lamp = new LampBlock("lamp");
    public static final Block battery = new BatteryBlock("battery");
    public static final Block coalGenerator = new CoalGeneratorBlock("coal_generator");
    public static final Block circuitFactory = new CircuitFactory("circuit_factory");

    @Side(Side.BOTH)
    public static void preInit() {
        Core.blocks.registerAll(testBlock, turretBlock, blueBeacon, redBeacon, minerBlock, itemDuctNorth, itemDuctSouth,
                itemDuctEast, itemDuctWest, inventoryPortal, unloaderBlock, wall, metalFormer, relayBlock, solarPanel,
                lamp, battery, coalGenerator, circuitFactory);
    }

    @Side(Side.CLIENT)
    public static void init() {
        BlockGroup defense = new BlockGroup("defense", Blocks.turretBlock, minerBlock, wall);
        BlockGroup misc = new BlockGroup("misc", testBlock, blueBeacon, metalFormer, circuitFactory);
        BlockGroup transport = new BlockGroup("transport", itemDuctNorth, unloaderBlock, inventoryPortal);
        BlockGroup power = new BlockGroup("power", relayBlock, solarPanel, lamp, battery, coalGenerator);

        Core.blockGroups.register(misc);
        Core.blockGroups.register(defense);
        Core.blockGroups.register(transport);
        Core.blockGroups.register(power);
    }
}
