package com.noahcharlton.robogeddon.block.miner;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.floor.MineableFloor;

public class MinerBlock extends Block implements HasTileEntity, BlockRenderer {

    private Array<TextureRegion> textures;

    public MinerBlock(String id) {
        super(id);
    }

    @Override
    public void initRenderer() {
        this.renderer = this;

        Core.assets.registerTextureGroup("blocks/miner").setOnLoad(t -> textures = t);
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                FloatUtils.asIntString(MinerTileEntity.TIME / 60f)
        };
    }

    @Override
    public boolean canBuildAt(Tile tile, Entity placer) {
        return super.canBuildAt(tile, placer) &&
                (tile.getUpperFloor() instanceof MineableFloor || tile.getFloor() instanceof MineableFloor);
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        renderTexture(batch, tile);
        renderItem(batch, tile);
    }

    private void renderItem(SpriteBatch batch, Tile tile) {
        var tileEntity = (MinerTileEntity) tile.getTileEntity();

        float innerX = Math.min(tileEntity.getTime() / MinerTileEntity.TIME * 22, 20);
        float x = tile.getPixelX() + innerX;
        float y = tile.getPixelY() + 10;
        batch.draw(tileEntity.getItem().getTinyTexture(), x, y);
    }

    private void renderTexture(SpriteBatch batch, Tile tile) {
        var index = (int) (System.currentTimeMillis() % (textures.size * 500)) / 500;

        batch.draw(textures.get(index), tile.getPixelX(), tile.getPixelY());
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        batch.setColor(1f, 1f, 1f, .5f);
        renderTexture(batch, tile);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    @Override
    public String getDisplayName() {
        return "AutoMiner";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new MinerTileEntity(tile);
    }
}
