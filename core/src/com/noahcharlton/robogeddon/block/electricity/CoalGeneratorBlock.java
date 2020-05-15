package com.noahcharlton.robogeddon.block.electricity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockRenderer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.GeneratorTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.inventory.SingleItemBuffer;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;
import com.noahcharlton.robogeddon.world.item.Items;

public class CoalGeneratorBlock extends Block implements HasTileEntity, BlockRenderer {

    private TextureRegion textureOn;
    private TextureRegion textureOff;

    public CoalGeneratorBlock(String id) {
        super(id);
    }

    @Override
    public void initRenderer() {
        Core.assets.registerTexture("blocks/coal_generator_on").setOnLoad(t -> textureOn = t);
        Core.assets.registerTexture("blocks/coal_generator_off").setOnLoad(t -> textureOff = t);

        this.renderer = this;
    }

    @Override
    public void render(SpriteBatch batch, Tile tile) {
        GeneratorTileEntity entity = (GeneratorTileEntity) tile.getTileEntity();
        var texture = entity.isGenerating() ? textureOn : textureOff;

        batch.draw(texture, tile.getPixelX(), tile.getPixelY());
    }

    @Override
    public void buildRender(SpriteBatch batch, Tile tile) {
        batch.setColor(1f, 1f, 1f, .5f);
        batch.draw(textureOff, tile.getPixelX(), tile.getPixelY());
        batch.setColor(1f, 1f, 1f, 1f);
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
        return "Coal Generator";
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new CoalGeneratorTileEntity(tile);
    }

    static class CoalGeneratorTileEntity extends GeneratorTileEntity implements HasInventory {

        private ItemBuffer items;
        private int burnTime;

        public CoalGeneratorTileEntity(Tile rootTile) {
            super(rootTile, 20);

            this.items = new SingleItemBuffer(Items.coal, 10);
        }

        @Override
        public void update() {
            super.update();

            if(world.isClient())
                return;

            burnTime--;

            if(burnTime <= 0){
                startBurning();
            }

            if(generating && burnTime <= 0){
                dirty = true;
                generating = false;
            }
        }

        private void startBurning() {
            var coal = items.retrieveItem();

            if(coal != null){
                burnTime = 300;
                generating = true;
                dirty = true;
            }
        }

        @Override
        public boolean acceptItem(Item item, Direction from) {
            if(items.acceptItem(item)){
                dirty = true;
                return true;
            }

            return false;
        }

        @Override
        public float[] getData() {
            return new float[]{
                    generating ? 1f : 0f,
            };
        }

        @Override
        public void receiveData(float[] data) {
            if(data.length > 0)
                generating = data[0] == 1f;
        }

        @Override
        public void setBuffers(ItemBuffer[] buffers) {
            items = buffers[0];
        }

        @Override
        public Item retrieveItem(boolean simulate, Direction to) {
            return null;
        }

        @Override
        public ItemBuffer[] getBuffers() {
            return new ItemBuffer[]{items};
        }
    }
}
