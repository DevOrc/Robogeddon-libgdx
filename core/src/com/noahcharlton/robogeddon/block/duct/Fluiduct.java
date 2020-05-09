package com.noahcharlton.robogeddon.block.duct;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.fluid.FluidBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.world.Tile;

import java.util.function.Consumer;

public class Fluiduct extends Block implements BlockRenderer, HasTileEntity {

    private static TextureRegion centerTexture;
    private static TextureRegion northTexture;
    private static TextureRegion eastTexture;
    private static TextureRegion southTexture;
    private static TextureRegion westTexture;

    private static TextureRegion centerFluidTexture;
    private static TextureRegion northFluidTexture;
    private static TextureRegion eastFluidTexture;
    private static TextureRegion southFluidTexture;
    private static TextureRegion westFluidTexture;


    public Fluiduct(String id) {
        super(id);
    }

    @Override
    public String getDisplayName() {
        return "Fluiduct";
    }

    @Override
    public void initRenderer() {
        this.renderer = this;

        registerSide("center", t -> centerTexture = t, t -> centerFluidTexture = t);
        registerSide("north", t -> northTexture = t, t -> northFluidTexture = t);
        registerSide("east", t -> eastTexture = t, t -> eastFluidTexture = t);
        registerSide("south", t -> southTexture = t, t -> southFluidTexture = t);
        registerSide("west", t -> westTexture = t, t -> westFluidTexture = t);
    }

    private void registerSide(String name, Consumer<TextureRegion> base, Consumer<TextureRegion> fluid){
        Core.assets.registerTexture("blocks/fluiduct_" + name).setOnLoad(base);
        Core.assets.registerTexture("blocks/fluiduct_" + name + "_fluid").setOnLoad(fluid);
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        var tileEntity = (FluiductTileEntity) tile.getTileEntity();

        render(batch, tile, centerTexture);
        drawBranches(batch, tile, tileEntity, false);

        var buffer = tileEntity.getInputBuffer();

        if(buffer.getFluid() != null){
            batch.setColor(getFluidColor(buffer));
            render(batch, tile, centerFluidTexture);
            drawBranches(batch, tile, tileEntity, true);
            batch.setColor(Color.WHITE);
        }
    }

    public Color getFluidColor(FluidBuffer buffer) {
        var color = buffer.getFluid().getColor(buffer.getAmount() / buffer.getCapacity());
        color.a = MathUtils.clamp(color.a, 0f,  1f);

        return color;
    }

    private void render(SpriteBatch batch, Tile tile, TextureRegion texture){
        var x = tile.getPixelX();
        var y = tile.getPixelY();

        batch.draw(texture, x, y);
    }

    private void drawBranches(SpriteBatch batch, Tile tile, FluiductTileEntity tileEntity, boolean fluid) {
        if(tileEntity.isConnectedNorth()){
            render(batch, tile, fluid ? northFluidTexture : northTexture);
        }
        if(tileEntity.isConnectedSouth()){
            render(batch, tile, fluid ? southFluidTexture : southTexture);
        }
        if(tileEntity.isConnectedWest()){
            render(batch, tile, fluid ? westFluidTexture : westTexture);
        }
        if(tileEntity.isConnectedEast()){
            render(batch, tile, fluid ? eastFluidTexture : eastTexture);
        }
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        batch.setColor(1f, 1f, 1f, .5f);
        render(batch, tile, centerTexture);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new FluiductTileEntity(tile);
    }
}
