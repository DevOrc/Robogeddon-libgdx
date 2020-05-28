package com.noahcharlton.robogeddon.block.turret;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class LaserBlock extends Block implements BlockRenderer, HasTileEntity {

    private TextureRegion baseTexture;
    private TextureRegion topTexture;

    public LaserBlock(String id) {
        super(id);
    }

    @Override
    protected void preInit() {
        requiredBlocks = List.of(Blocks.relayBlock);
        requirements = List.of(Items.iron.stack(100), Items.circuit.stack(5));
    }

    @Override
    public void initRenderer() {
        this.renderer = this;

        Core.assets.registerTexture("blocks/laser_base").setOnLoad(t -> baseTexture = t);
        Core.assets.registerTexture("blocks/laser_top").setOnLoad(t -> topTexture = t);
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                FloatUtils.asIntString(LaserTileEntity.DAMAGE * 60),
                FloatUtils.asIntString(LaserTileEntity.POWER_USE),
                FloatUtils.asIntString(LaserTileEntity.RANGE / Tile.SIZE)
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
        return "Laser";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new LaserTileEntity(tile);
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        var tileEntity = (LaserTileEntity) tile.getTileEntity();

        renderTexture(batch, tile, tileEntity.getLaserAngle());

        if(tileEntity.getTarget()  != null && tileEntity.hasPower()){
            drawLaser(tile, tileEntity.getTarget(), tileEntity.getLaserAngle());
        }
    }

    private void drawLaser(Tile tile, Entity target, float laserAngle) {
        var shapeRenderer = Core.client.getGameShapeDrawer();
        var laserArmLength = 28;
        laserAngle *= Math.PI / 180;
        shapeRenderer.setColor(new Color(0xCC0000FF));

        //Add on an extra tile, because this is a 2x2 block
        float tileX = (float) (tile.getPixelX() + Tile.SIZE + (Math.cos(laserAngle) * laserArmLength));
        float tileY = (float) (tile.getPixelY() + Tile.SIZE + (Math.sin(laserAngle) * laserArmLength));


        shapeRenderer.line(tileX, tileY, target.getX(), target.getY(), 4);
        shapeRenderer.filledCircle(target.getX(), target.getY(), 2);
    }

    private void renderTexture(SpriteBatch batch, Tile tile, float angle) {
        float x = tile.getPixelX();
        float y = tile.getPixelY();

        batch.draw(baseTexture, x, y);
        GraphicsUtil.drawRotated(batch, topTexture, x, y, angle);
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        float angle = (System.currentTimeMillis() % 7200) / 20f;

        batch.setColor(1f, 1f, 1f, .5f);
        renderTexture(batch, tile, angle);
        batch.setColor(Color.WHITE);
    }
}
