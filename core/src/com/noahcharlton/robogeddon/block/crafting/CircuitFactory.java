package com.noahcharlton.robogeddon.block.crafting;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.SingleItemBuffer;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

public class CircuitFactory extends Block implements HasTileEntity {

    private static final int CRAFT_TIME = 90;
    private static final float POWER_USAGE = 3f;

    public CircuitFactory(String id) {
        super(id);
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                FloatUtils.asString(CRAFT_TIME / 60f, 1, 1),
                FloatUtils.asIntString(POWER_USAGE)
        };
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Override
    public String getDisplayName() {
        return "Circuit Factory";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new PoweredCrafterTileEntity(
                tile,
                POWER_USAGE,
                CRAFT_TIME,
                new SingleItemBuffer(Items.circuit, 10),
                new SingleItemBuffer(Items.sand, 10),
                new SingleItemBuffer(Items.iron, 10)
        );
    }
}
