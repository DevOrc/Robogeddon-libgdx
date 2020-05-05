package com.noahcharlton.robogeddon.block.crafting;

import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.SingleItemBuffer;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

public class CircuitFactory extends Block implements HasTileEntity {

    public CircuitFactory(String id) {
        super(id);
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
                3f,
                90,
                new SingleItemBuffer(Items.circuit, 10),
                new SingleItemBuffer(Items.sand, 10),
                new SingleItemBuffer(Items.iron, 10)
        );
    }
}
