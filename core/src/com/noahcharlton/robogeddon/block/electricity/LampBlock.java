package com.noahcharlton.robogeddon.block.electricity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.PoweredTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class LampBlock extends Block implements BlockRenderer, HasTileEntity {

    private static final float USAGE_RATE = .1f;

    private List<Block> requiredBlocks;
    private List<ItemStack> requiredItems;

    private TextureRegion off;
    private TextureRegion on;

    public LampBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requiredBlocks = List.of(Blocks.relayBlock);
        requiredItems =  List.of(Items.iron.stack(10));
    }

    @Override
    public List<Block> getRequiredBlocks() {
        return requiredBlocks;
    }

    @Override
    public List<ItemStack> getRequirements() {
        return requiredItems;
    }

    @Override
    public void initRenderer() {
        this.renderer = this;

        Core.assets.registerTexture("blocks/lamp_off").setOnLoad(t -> off = t);
        Core.assets.registerTexture("blocks/lamp_on").setOnLoad(t -> on = t);
    }

    @Override
    public String getDisplayName() {
        return "Lamp";
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                String.valueOf(USAGE_RATE)
        };
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        PoweredTileEntity tileEntity = (PoweredTileEntity) tile.getTileEntity();
        var texture = tileEntity.hasPower() ? on : off;

        batch.draw(texture, tile.getPixelX(), tile.getPixelY());
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        batch.setColor(1f, 1f, 1f, .5f);
        var texture = System.currentTimeMillis() % 1500 > 750 ? off : on;
        batch.draw(texture, tile.getPixelX(), tile.getPixelY());
        batch.setColor(Color.WHITE);
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new LampTileEntity(tile, USAGE_RATE);
    }

    static class LampTileEntity extends PoweredTileEntity {

        LampTileEntity(Tile rootTile, float usageRate) {
            super(rootTile, usageRate);
        }

        @Override
        public void update() {
            super.update();

            if(world.isServer())
                usePower();
        }
    }
}
