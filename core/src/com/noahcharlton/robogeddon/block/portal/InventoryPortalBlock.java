package com.noahcharlton.robogeddon.block.portal;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.world.Tile;

import java.util.List;

public class InventoryPortalBlock extends Block implements BlockRenderer, HasTileEntity {

    private static TextureRegion baseTexture;
    private static TextureRegion topTexture;

    public InventoryPortalBlock(String id) {
        super(id);
    }

    @Override
    public void initRenderer() {
        this.renderer = this;

        Core.assets.registerTexture("blocks/inventory_portal_base").setOnLoad(t -> baseTexture = t);
        Core.assets.registerTexture("blocks/inventory_portal_top").setOnLoad(t -> topTexture = t);
    }

    @Override
    protected void preInit() {
        //Hack to make sure inventory portal is never unlocked
        requiredBlocks = List.of(Blocks.inventoryPortal);
    }

    @Override
    public String getDisplayName() {
        return "Inventory Portal";
    }

    @Override
    public float getHardness() {
        return 4.0f;
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        var angle = calculateAngle();

        batch.draw(baseTexture, tile.getPixelX(), tile.getPixelY());
        GraphicsUtil.drawRotated(batch, topTexture, tile.getPixelX(), tile.getPixelY(), angle);
    }

    private float calculateAngle() {
        return (System.currentTimeMillis() % 7200) / 20f;
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        batch.setColor(1f, 1f, 1f, .5f);
        render(batch, tile);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new InventoryPortalTileEntity(tile);
    }
}
