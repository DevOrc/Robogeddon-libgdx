package com.noahcharlton.robogeddon.block;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.beacon.BeaconBlock;
import com.noahcharlton.robogeddon.block.miner.MinerBlock;
import com.noahcharlton.robogeddon.block.turret.TurretBlock;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.team.Team;

public class Blocks{

    public static final Block testBlock = new TestBlock("test_block");
    public static final Block oilBlock = new OilBlock("oil");
    public static final Block turretBlock = new TurretBlock("turret");
    public static final Block blueBeacon = new BeaconBlock("beacon", Team.BLUE);
    public static final Block redBeacon = new BeaconBlock("beacon", Team.RED);
    public static final Block minerBlock = new MinerBlock("miner");

    @Side(Side.BOTH)
    public static void preInit() {
        Core.blocks.register(testBlock);
        Core.blocks.register(oilBlock);
        Core.blocks.register(turretBlock);
        Core.blocks.register(blueBeacon);
        Core.blocks.register(redBeacon);
        Core.blocks.register(minerBlock);
    }

    @Side(Side.CLIENT)
    public static void init() {
        BlockGroup defense = new BlockGroup("defense", Blocks.turretBlock, minerBlock);
        BlockGroup misc = new BlockGroup("misc", oilBlock, testBlock, blueBeacon, redBeacon);

        Core.blockGroups.register(misc);
        Core.blockGroups.register(defense);
    }
}
