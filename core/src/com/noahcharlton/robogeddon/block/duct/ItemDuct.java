package com.noahcharlton.robogeddon.block.duct;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.Blocks;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.util.help.HelpInfoLoader;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;
import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;
import java.util.function.Consumer;

public class ItemDuct extends Block implements BlockRenderer, HasTileEntity {

    private static boolean textureRegistered = false;

    private static TextureRegion centerTexture;
    private static TextureRegion centerRoofTexture;

    private static TextureRegion topWallTexture;
    private static TextureRegion rightWallTexture;
    private static TextureRegion leftWallTexture;
    private static TextureRegion bottomWallTexture;

    private static TextureRegion topRoofTexture;
    private static TextureRegion rightRoofTexture;
    private static TextureRegion leftRoofTexture;
    private static TextureRegion bottomRoofTexture;

    private final Direction direction;

    public ItemDuct(String id, Direction direction) {
        super(id + "_" + direction.name());

        this.direction = direction;
    }

    @Override
    public String getDisplayName() {
        return "Item Duct";
    }

    @Override
    public void initRenderer() {
        this.renderer = this;

        if(!textureRegistered){
            registerSide("center", t -> centerTexture = t, t -> centerRoofTexture = t);
            registerSide("top", t -> topWallTexture = t, t -> topRoofTexture = t);
            registerSide("bottom", t -> bottomWallTexture = t, t -> bottomRoofTexture = t);
            registerSide("left", t -> leftWallTexture = t, t -> leftRoofTexture = t);
            registerSide("right", t -> rightWallTexture = t, t -> rightRoofTexture = t);

            textureRegistered = true;
        }
    }

    @Override
    public List<ItemStack> getRequirements() {
        return List.of(Items.iron.stack(1), Items.rock.stack(1));
    }

    @Override
    protected void createHelpInfo() {
        helpInfo = HelpInfoLoader.getEntry(Blocks.itemDuctID);
    }

    private static void registerSide(String name, Consumer<TextureRegion> wall, Consumer<TextureRegion> roof){
        Core.assets.registerTexture("blocks/item_duct_wall_" + name).setOnLoad(wall);
        Core.assets.registerTexture("blocks/item_duct_roof_" + name).setOnLoad(roof);
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        var tileEntity = (ItemDuctTileEntity) tile.getTileEntity();

        render(batch, tile, centerTexture);
        drawBranches(batch, tile, tileEntity, true);
    }

    @Override
    public void renderLayer(SpriteBatch batch, Tile tile, int layer) {
        var tileEntity = (ItemDuctTileEntity) tile.getTileEntity();

        if(layer == 1) {
            drawItems(batch, tile, tileEntity);
        }else if(layer == 2){
            batch.setColor(1f, 1f, 1f, 84f / 255f);
            render(batch, tile, centerRoofTexture);
            drawBranches(batch, tile, tileEntity, false);
            batch.setColor(1f, 1f, 1f, 1f);
        }
    }

    private void drawItems(SpriteBatch batch, Tile tile, ItemDuctTileEntity entity) {
        for(int i = 0; i < entity.getBuffers().length; i++){
            var item = entity.getBuffers()[i];
            var x = entity.getItemXs()[i] - Item.ICON_RADIUS + tile.getPixelX();
            var y = entity.getItemYs()[i] - Item.ICON_RADIUS + tile.getPixelY();

            if(item.currentItem() != null)
                batch.draw(item.currentItem().getTinyTexture(), x, y);
        }
    }

    private void render(SpriteBatch batch, Tile tile, TextureRegion texture){
        var x = tile.getPixelX();
        var y = tile.getPixelY();

        batch.draw(texture, x, y);
    }

    private void drawBranches(SpriteBatch batch, Tile tile, ItemDuctTileEntity tileEntity, boolean walls) {
        if(tileEntity.isConnectNorth()){
            render(batch, tile, walls ? topWallTexture : topRoofTexture);
        }
        if(tileEntity.isConnectSouth()){
            render(batch, tile, walls ? bottomWallTexture : bottomRoofTexture);
        }
        if(tileEntity.isConnectWest()){
            render(batch, tile, walls ? leftWallTexture : leftRoofTexture);
        }
        if(tileEntity.isConnectEast()){
            render(batch, tile, walls ? rightWallTexture : rightRoofTexture);
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
        return new ItemDuctTileEntity(tile, direction);
    }

    public Direction getDirection() {
        return direction;
    }
}
