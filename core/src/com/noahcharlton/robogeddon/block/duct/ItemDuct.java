package com.noahcharlton.robogeddon.block.duct;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.World;

public class ItemDuct extends Block implements BlockRenderer {

    private static boolean textureRegistered = false;
    private static TextureRegion centerTexture;
    private static TextureRegion topTexture;
    private static TextureRegion rightTexture;
    private static TextureRegion leftTexture;
    private static TextureRegion bottomTexture;

    private final Direction direction;

    public ItemDuct(String id, Direction direction) {
        super(id + "_" + direction.name());

        this.direction = direction;
    }

    @Override
    public void initRenderer() {
        this.renderer = this;

        if(!textureRegistered){
            Core.assets.registerTexture("blocks/item_duct").setOnLoad(t -> centerTexture = t);
            Core.assets.registerTexture("blocks/item_duct_top").setOnLoad(t -> topTexture = t);
            Core.assets.registerTexture("blocks/item_duct_right").setOnLoad(t -> rightTexture = t);
            Core.assets.registerTexture("blocks/item_duct_left").setOnLoad(t -> leftTexture = t);
            Core.assets.registerTexture("blocks/item_duct_bottom").setOnLoad(t -> bottomTexture = t);
            textureRegistered = true;
        }
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        render(batch, tile, centerTexture);
        drawBranches(batch, tile);
    }

    private void render(SpriteBatch batch, Tile tile, TextureRegion texture){
        var x = tile.getPixelX();
        var y = tile.getPixelY();

        batch.draw(texture, x, y);
    }

    private void drawBranches(SpriteBatch batch, Tile tile) {
        if(isItemDuct(tile.getX(), tile.getY() + 1, tile.getWorld())){
            render(batch, tile, topTexture);
        }
        if(isItemDuct(tile.getX(), tile.getY() - 1, tile.getWorld())){
            render(batch, tile, bottomTexture);
        }
        if(isItemDuct(tile.getX() - 1, tile.getY(), tile.getWorld())){
            render(batch, tile, leftTexture);
        }
        if(isItemDuct(tile.getX() + 1, tile.getY(), tile.getWorld())){
            render(batch, tile, rightTexture);
        }
    }

    private boolean isItemDuct(int tileX, int tileY, World world) {
        var tile = world.getTileAt(tileX, tileY);

        return tile != null && tile.getBlock() instanceof ItemDuct;
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        batch.setColor(1f, 1f, 1f, .5f);
        render(batch, tile);
        batch.setColor(1f, 1f, 1f, 1f);
    }
}
