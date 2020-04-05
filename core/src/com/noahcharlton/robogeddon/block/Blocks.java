package com.noahcharlton.robogeddon.block;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.turret.TurretBlock;
import com.noahcharlton.robogeddon.util.Side;

public class Blocks{

    public static final Block testBlock = new TestBlock("test_block");
    public static final Block oilBlock = new OilBlock("oil");
    public static final Block turret = new TurretBlock("turret");

    @Side(Side.BOTH)
    public static void preInit() {
        Core.blocks.register(testBlock);
        Core.blocks.register(oilBlock);
        Core.blocks.register(turret);
    }

    @Side(Side.CLIENT)
    public static void init() {
    }
}
