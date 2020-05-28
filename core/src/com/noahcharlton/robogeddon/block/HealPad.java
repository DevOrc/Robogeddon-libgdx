package com.noahcharlton.robogeddon.block;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.block.tileentity.electricity.PoweredTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.HasTileEntity;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.util.FloatUtils;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;

public class HealPad extends Block implements HasTileEntity {

    private static final float usageRate = 10f;
    private static final float healRate = .1f;
    private static final float range = Tile.SIZE * 12;

    public HealPad(String id) {
        super(id);
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

                if(target.getHealth() >= target.getType().getHealth()){
                    target = null;
                }
            }
        }

        private void findTarget() {
            var serverWorld = (ServerWorld) world;

            for(Entity player : serverWorld.getPlayers().values()){
                if(player.getHealth() < player.getType().getHealth()){
                    target = player;
                    return;
                }
            }
        }
    }
}
