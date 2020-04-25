package com.noahcharlton.robogeddon.block.turret;

import com.badlogic.gdx.math.Vector2;
import com.noahcharlton.robogeddon.block.tileentity.HasInventory;
import com.noahcharlton.robogeddon.block.tileentity.ItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.SingleItemBuffer;
import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.Item;
import com.noahcharlton.robogeddon.world.item.Items;
import com.noahcharlton.robogeddon.world.team.Team;

public class TurretTileEntity extends TileEntity implements HasInventory {

    private static float RANGE = 650;
    private static int SHOOTER_TIME = 30;

    private ItemBuffer ammo;
    private Entity target;
    private float angle;
    private int shooterTime;

    public TurretTileEntity(Tile rootTile) {
        super(rootTile);

        ammo = new SingleItemBuffer(Items.iron, 5);
    }

    @Override
    public void update() {
        if(target == null || target.isDead()){
            findTarget();
            angle += .005;
            shooterTime = SHOOTER_TIME;
            return;
        }else if(!isInRange(target) || world.isClient()){
            target = null;
            return;
        }

        var vec = new Vector2(target.getX(), target.getY()).sub(rootTile.getPixelX(), rootTile.getPixelY());
        angle = vec.angleRad();
        dirty = true;
        shooterTime--;

        if(shooterTime < 0 && ammo.getAmount() > 0){
            shooterTime = SHOOTER_TIME;
            ammo.retrieveItem();
            shoot();
        }
    }

    @Side(Side.SERVER)
    private void shoot() {
        var team = getRootTile().getChunk().getTeam();
        var entity = EntityType.bulletEntity.create(world, team);
        entity.setAngle(angle);
        entity.setX(rootTile.getPixelX());
        entity.setY(rootTile.getPixelY());

        ((ServerWorld) world).addEntity(entity);
    }

    @Side(Side.SERVER)
    private void findTarget() {
        var team = getRootTile().getChunk().getTeam();

        if(world.isClient() || team == Team.NEUTRAL)
            return;

        for(Entity entity: world.getEntities()){
            if(entity.getTeam() == team || !entity.getType().isTargetable())
                continue;

            if(isInRange(entity)){
                target = entity;
                return;
            }
        }
    }

    private boolean isInRange(Entity entity) {
        return new Vector2(entity.getX(), entity.getY())
                        .sub(rootTile.getPixelX(), rootTile.getPixelY()).len2() < RANGE * RANGE;
    }

    @Side(Side.SERVER)
    @Override
    public float[] getData() {
        return new float[]{angle};
    }

    @Side(Side.CLIENT)
    @Override
    public void receiveData(float[] data) {
        angle = data[0];
    }

    public float getAngle() {
        return angle;
    }

    @Override
    public boolean acceptItem(Item item) {
        return ammo.acceptItem(item);
    }

    @Override
    public void setBuffers(ItemBuffer[] buffers) {
        ammo = buffers[0];
    }

    @Override
    public Item retrieveItem(boolean simulate) {
        return null;
    }

    @Override
    public ItemBuffer[] getBuffers() {
        return new ItemBuffer[]{ammo};
    }
}
