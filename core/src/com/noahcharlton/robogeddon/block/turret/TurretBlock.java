package com.noahcharlton.robogeddon.block.turret;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.Arrays;
import java.util.List;

public class TurretBlock extends Block implements BlockRenderer, HasTileEntity {

    private TextureRegion baseTexture;
    private TextureRegion topTexture;

    public TurretBlock(String id) {
        super(id);
    }

    @Override
    public List<ItemStack> getRequirements() {
        return Arrays.asList(Items.rock.stack(25), Items.iron.stack(5));
    }

    @Override
    public String getDisplayName() {
        return "Turret";
    }

    @Override
    public void initRenderer() {
        Core.assets.registerTexture("blocks/turret_base").setOnLoad(texture -> baseTexture = texture);
        Core.assets.registerTexture("blocks/turret_top").setOnLoad(texture -> topTexture = texture);

        renderer = this;
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new TurretTileEntity(tile);
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        TurretTileEntity tileEntity = (TurretTileEntity) tile.getTileEntity();
        float x = tile.getPixelX();
        float y = tile.getPixelY();
        float degrees = (float) (tileEntity.getAngle() * 180 / Math.PI);

        batch.draw(baseTexture, x, y);
        GraphicsUtil.drawRotated(batch, topTexture, x, y, degrees);
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        float x = tile.getPixelX();
        float y = tile.getPixelY();

        batch.setColor(1f, 1f, 1f, .5f);
        batch.draw(baseTexture, x, y);
        batch.draw(topTexture, x, y);
        batch.setColor(1f, 1f, 1f, 1f);
    }
}
