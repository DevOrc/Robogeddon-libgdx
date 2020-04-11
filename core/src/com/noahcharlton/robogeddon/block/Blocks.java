package com.noahcharlton.robogeddon.block;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.beacon.BeaconBlock;
import com.noahcharlton.robogeddon.block.duct.ItemDuct;
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
    public static final Block oilBlock = new OilBlock("oil");
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

    @Side(Side.BOTH)
    public static void preInit() {
        Core.blocks.register(testBlock);
        Core.blocks.register(oilBlock);
        Core.blocks.register(turretBlock);
        Core.blocks.register(blueBeacon);
        Core.blocks.register(redBeacon);
        Core.blocks.register(minerBlock);
        Core.blocks.register(itemDuctNorth);
        Core.blocks.register(itemDuctSouth);
        Core.blocks.register(itemDuctEast);
        Core.blocks.register(itemDuctWest);
        Core.blocks.register(inventoryPortal);
        Core.blocks.register(unloaderBlock);
    }

    @Side(Side.CLIENT)
    public static void init() {
        BlockGroup defense = new BlockGroup("defense", Blocks.turretBlock, minerBlock, unloaderBlock);
        BlockGroup misc = new BlockGroup("misc", oilBlock, testBlock, blueBeacon, redBeacon);
        BlockGroup transport = new BlockGroup("transport",
                itemDuctNorth, itemDuctEast, itemDuctSouth, itemDuctWest, inventoryPortal);

        Core.blockGroups.register(misc);
        Core.blockGroups.register(defense);
        Core.blockGroups.register(transport);
    }
}
