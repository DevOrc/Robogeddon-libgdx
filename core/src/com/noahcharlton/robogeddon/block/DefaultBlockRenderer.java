package com.noahcharlton.robogeddon.block;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;

import java.util.function.BiConsumer;

@Side(Side.CLIENT)
public class DefaultBlockRenderer implements BlockRenderer {

    private BiConsumer<Batch, Tile> renderSelected = (batch, tile) -> {};
    private TextureRegion texture;

    public DefaultBlockRenderer(Block block) {
        var path = "blocks/" + block.getTypeID();

        Core.assets.registerTexture(path).setOnLoad(t -> texture = t);
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        float x = tile.getPixelX();
        float y = tile.getPixelY();

        batch.draw(texture, x, y);
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        batch.setColor(1f, 1f, 1f, .5f);
        render(batch, tile);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    @Override
    public void renderSelected(SpriteBatch batch, Tile tile) {
        renderSelected.accept(batch, tile);
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setRenderSelected(BiConsumer<Batch, Tile> renderSelected) {
        this.renderSelected = renderSelected;
    }
}
