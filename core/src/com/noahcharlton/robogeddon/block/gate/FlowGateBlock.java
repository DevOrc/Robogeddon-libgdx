package com.noahcharlton.robogeddon.block.gate;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.DefaultBlockRenderer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class FlowGateBlock extends Block implements HasTileEntity {

    public FlowGateBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requirements = List.of(Items.iron.stack(10), Items.circuit.stack(2));
    }

    @Override
    public String getDisplayName() {
        return "Flow Gate";
    }

    public TextureRegion getTexture(){
        return ((DefaultBlockRenderer) renderer).getTexture();
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new FlowGateTileEntity(tile);
    }
}
