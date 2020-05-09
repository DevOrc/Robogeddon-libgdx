package com.noahcharlton.robogeddon.block.gate;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.DefaultBlockRenderer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;

public class FlowGateBlock extends Block implements HasTileEntity {

    public FlowGateBlock(String id) {
        super(id);
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
