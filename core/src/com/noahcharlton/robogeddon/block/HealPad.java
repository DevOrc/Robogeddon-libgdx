package com.noahcharlton.robogeddon.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.PoweredTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Items;

import java.util.List;

public class HealPad extends Block implements HasTileEntity {

    private static final float usageRate = 10f;
    private static final float healRate = .1f;
    private static final float range = Tile.SIZE * 12;

    public HealPad(String id) {
        super(id);
    }

    @Override
    protected void initRenderer() {
        super.initRenderer();

        ((DefaultBlockRenderer) renderer).setRenderSelected(this::renderSelected);
    }

    @Override
    protected void preInit() {
        requiredBlocks = List.of(Blocks.relayBlock);
        requirements = List.of(Items.iron.stack(100), Items.circuit.stack(5));
    }

    @Override
    public String[] getDescriptionParameters() {
        return new String[]{
                FloatUtils.asIntString(range / Tile.SIZE),
                FloatUtils.asIntString(usageRate),
                FloatUtils.asString(healRate, 1, 1)
        };
    }

    @Override
    public String getDisplayName() {
        return "Heal Pad";
    }

    private void renderSelected(Batch batch, Tile tile) {
        var sr = Core.client.getGameShapeDrawer();
        sr.setColor(Color.WHITE);
        sr.setDefaultLineWidth(4);

        sr.circle(tile.getPixelXCenter(), tile.getPixelYCenter(), range);
    }

    @Override
    public TileEntity createTileEntity(Tile tile) {
        return new HealPadTileEntity(tile, usageRate);
    }

    static class HealPadTileEntity extends PoweredTileEntity {

        private Entity target;

        public HealPadTileEntity(Tile rootTile, float usageRate) {
            super(rootTile, usageRate);
        }

        @Override
        public void update() {
            super.update();

            if(world.isServer()){
                updateServer();
            }
        }

        private void updateServer() {
            if(target == null){
                findTarget();
            }else{
                usePower();

                if(hasPower()){
                    target.damage(-healRate);
                }

                if(target.getHealth() >= target.getType().getHealth() || !isInRange(target)){
                    target = null;
                }
            }
        }

        private void findTarget() {
            var serverWorld = (ServerWorld) world;

            for(Entity player : serverWorld.getPlayers().values()){
                boolean needsHealth = player.getHealth() < player.getType().getHealth();
                boolean inRange = isInRange(player);

                if(needsHealth && inRange){
                    target = player;
                    return;
                }
            }
        }

        private boolean isInRange(Entity target) {
            Vector2 pos = new Vector2(target.getX(), target.getY())
                    .sub(rootTile.getPixelXCenter(), rootTile.getPixelYCenter());

            return pos.len2() < (HealPad.range * HealPad.range);
        }
    }
}
