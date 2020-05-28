package com.noahcharlton.robogeddon.block.turret;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.entity.BulletEntity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class TurretBlock extends Block implements BlockRenderer, HasTileEntity {

    private TextureRegion baseTexture;
    private TextureRegion topTexture;

    public TurretBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requirements = List.of(Items.rock.stack(25), Items.iron.stack(45));
    }

    @Override
    public String getDisplayName() {
        return "Turret";
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                Integer.toString(TurretTileEntity.SHOOT_TILE_RANGE),
                FloatUtils.asString(TurretTileEntity.SHOOTER_TIME / 60f, 1, 1),
                FloatUtils.asIntString(BulletEntity.ENTITY_DAMAGE)
        };
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
    public void renderSelected(SpriteBatch batch, Tile tile) {
        var shapeDrawer = Core.client.getGameShapeDrawer();
        var x = tile.getPixelX() + (Tile.SIZE / 2f);
        var y = tile.getPixelY() + (Tile.SIZE / 2f);
        shapeDrawer.setColor(Color.WHITE);
        shapeDrawer.circle(x, y, TurretTileEntity.RANGE);
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
