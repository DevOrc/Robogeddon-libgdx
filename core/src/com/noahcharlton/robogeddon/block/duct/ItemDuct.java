package com.noahcharlton.robogeddon.block.duct;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.World;

public class ItemDuct extends Block implements BlockRenderer {

    private static Color innerBorder = new Color(0x7E0101FF);
    private static Color outerBorder = new Color(0x403F3FFF);
    private static Color bottom = new Color(0x6B6B6BFF);
    private static Color glassColor = new Color(0x207EB554);

    private static boolean textureRegistered = false;
    private static TextureRegion texture;

    private final Direction direction;

    public ItemDuct(String id, Direction direction) {
        super(id + "_" + direction.name());

        this.direction = direction;
    }

    @Override
    public void initRenderer() {
        this.renderer = this;

        if(!textureRegistered){
            Core.assets.registerTexture("blocks/item_duct").setOnLoad(t -> texture = t);
            textureRegistered = true;
        }
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        var x = tile.getPixelX();
        var y = tile.getPixelY();

        batch.draw(texture, x, y);
        drawBranches(tile);
    }

    private void drawBranches(Tile tile) {
        var pixelX = tile.getPixelX();
        var pixelY = tile.getPixelY();
        var shapeDrawer = Core.client.getGameShapeDrawer();

        if(isItemDuct(tile.getX(), tile.getY() + 1, tile.getWorld())){
            shapeDrawer.filledRectangle(pixelX + 4, pixelY + 26, 22, 6, bottom);
            shapeDrawer.filledRectangle(pixelX + 2, pixelY + 28, 2, 4, outerBorder);
            shapeDrawer.filledRectangle(pixelX + 28, pixelY + 28, 2, 4, outerBorder);
            shapeDrawer.filledRectangle(pixelX + 4, pixelY + 26, 2, 6, innerBorder);
            shapeDrawer.filledRectangle(pixelX + 26, pixelY + 26, 2, 6, innerBorder);
            shapeDrawer.filledRectangle(pixelX + 6, pixelY + 26, 20, 6, glassColor);
        }

        if(isItemDuct(tile.getX(), tile.getY() - 1, tile.getWorld())){
            shapeDrawer.filledRectangle(pixelX + 4, pixelY, 22, 6, bottom);
            shapeDrawer.filledRectangle(pixelX + 2, pixelY, 2, 4, outerBorder);
            shapeDrawer.filledRectangle(pixelX + 28, pixelY, 2, 4, outerBorder);
            shapeDrawer.filledRectangle(pixelX + 4, pixelY, 2, 6, innerBorder);
            shapeDrawer.filledRectangle(pixelX + 26, pixelY, 2, 6, innerBorder);
            shapeDrawer.filledRectangle(pixelX + 6, pixelY, 20, 6, glassColor);
        }


        if(isItemDuct(tile.getX() - 1, tile.getY(), tile.getWorld())){
            shapeDrawer.filledRectangle(pixelX, pixelY + 6, 6, 20, bottom);
            shapeDrawer.filledRectangle(pixelX, pixelY + 2, 4, 2, outerBorder);
            shapeDrawer.filledRectangle(pixelX, pixelY + 28, 4, 2, outerBorder);
            shapeDrawer.filledRectangle(pixelX, pixelY + 4, 4, 2, innerBorder);
            shapeDrawer.filledRectangle(pixelX, pixelY + 26, 4, 2, innerBorder);
            shapeDrawer.filledRectangle(pixelX, pixelY + 6, 6, 20, glassColor);
        }

        if(isItemDuct(tile.getX() + 1, tile.getY(), tile.getWorld())){
            shapeDrawer.filledRectangle(pixelX + 26, pixelY + 6, 6, 20, bottom);
            shapeDrawer.filledRectangle(pixelX + 28, pixelY + 2, 4, 2, outerBorder);
            shapeDrawer.filledRectangle(pixelX + 28, pixelY + 28, 4, 2, outerBorder);
            shapeDrawer.filledRectangle(pixelX + 28, pixelY + 4, 4, 2, innerBorder);
            shapeDrawer.filledRectangle(pixelX + 28, pixelY + 26, 4, 2, innerBorder);
            shapeDrawer.filledRectangle(pixelX + 26, pixelY + 6, 6, 20, glassColor);
        }
    }

    private boolean isItemDuct(int tileX, int tileY, World world) {
        var tile = world.getTileAt(tileX, tileY);

        return tile != null && tile.getBlock() instanceof ItemDuct;
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        var x = tile.getPixelX();
        var y = tile.getPixelY();

        batch.setColor(1f, 1f, 1f, .5f);
        batch.draw(texture, x, y);
        batch.setColor(1f, 1f, 1f, 1f);
    }
}
