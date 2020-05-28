package com.noahcharlton.robogeddon.block.crafting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.SingleItemBuffer;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class MetalFormerBlock extends Block implements HasTileEntity, BlockRenderer {

    private static final int CRAFT_TIME = 80;

    private TextureRegion baseTexture;
    private TextureRegion tankTexture;

    public MetalFormerBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requiredBlocks = List.of(Blocks.turretBlock);
        requirements = List.of(Items.iron.stack(40));
    }

    @Override
    public String getDisplayName() {
        return "Bullet Factory";
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                FloatUtils.asString(CRAFT_TIME / 60f, 1, 1)
        };
    }

    @Override
    public void initRenderer() {
        Core.assets.registerTexture("blocks/metal_former_base").setOnLoad(t -> baseTexture = t);
        Core.assets.registerTexture("blocks/metal_former_tanks").setOnLoad(t -> tankTexture = t);

        this.renderer = this;
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
    public TileEntity createTileEntity(Tile tile) {
        return new CrafterTileEntity(
                tile,
                CRAFT_TIME,
                new SingleItemBuffer(Items.ironBullet, 10),
                new SingleItemBuffer(Items.iron, 10)
        );
    }


    @Override
    public void render(SpriteBatch batch, Tile tile) {
        var tileEntity = (CrafterTileEntity) tile.getTileEntity();

        batch.draw(baseTexture, tile.getPixelX(), tile.getPixelY());

        batch.setColor(getTankColor(tileEntity.getTick()));
        batch.draw(tankTexture, tile.getPixelX(), tile.getPixelY());
        batch.setColor(1f, 1f, 1f, 1f);
    }

    private Color getTankColor(float tick) {
        if(tick == 0){
            return new Color(1f, 1f, 1f, .4f);
        }

        float alpha = -.0003f * tick * (tick - 90f);

        return new Color(1f, 1f, 1f, MathUtils.clamp(alpha, .4f, 1f));
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        batch.setColor(1f, 1f, 1f, .5f);
        batch.draw(baseTexture, tile.getPixelX(), tile.getPixelY());
        batch.setColor(1f, 1f, 1f, 1f);
    }
}
