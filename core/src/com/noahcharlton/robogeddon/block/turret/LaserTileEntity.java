package com.noahcharlton.robogeddon.block.turret;

import com.badlogic.gdx.math.Vector2;
import com.noahcharlton.robogeddon.block.tileentity.electricity.PoweredTileEntity;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.team.Team;

public class LaserTileEntity extends PoweredTileEntity {

    private static final float POWER_USE = 50f;
    private static final float RANGE = 1024f;

    private Entity target;

    @Side(Side.CLIENT)
    private float angle;

    public LaserTileEntity(Tile rootTile) {
        super(rootTile, POWER_USE);
    }

    @Override
    public void update() {
        super.update();

        if(world.isServer()){
            updateServer();
        }else{
            updateClient();
        }
    }

    public void updateServer(){
        if(target == null){
            findTarget();
        }else if(createTargetVector(target).len2() > RANGE * RANGE
                || getTeam() == Team.NEUTRAL
                || target.getTeam() == getTeam()
                || target.isDead()){
            target = null;
            dirty = true;
        }else{
            usePower();

            if(hasPower()){
                target.damage(.5f);
            }
        }
    }

    private void findTarget() {
        if(getTeam() == Team.NEUTRAL)
            return;

        for(Entity entity : world.getEntities()){
            if(!entity.getType().isTargetable())
                continue;

            if(createTargetVector(entity).len2() < RANGE * RANGE && entity.getTeam() != getTeam()){
                target = entity;
                dirty = true;
                return;
            }
        }
    }

    private Team getTeam() {
        return getRootTile().getChunk().getTeam();
    }

    private Vector2 createTargetVector(Entity target) {
        return new Vector2(target.getX(), target.getY())
                .sub(rootTile.getPixelX(), rootTile.getPixelY());
    }

    public void updateClient(){
        if(target != null){
            var vec = createTargetVector(target);

            angle = vec.angle();
        }else{
            angle += .25f;
        }
    }

    @Override
    public float[] getData() {
        return new float[]{hasPower() ? 1f : 0f, target == null ? -1 : target.getId()};
    }

    @Override
    public void receiveData(float[] data) {
        super.receiveData(data);

        int targetID = (int) data[1];

        if(targetID >= 0){
            target = world.getEntityByID(targetID);
        }else{
            target = null;
        }

    }

    public float getLaserAngle() {
        return angle;
    }

    public Entity getTarget() {
        return target;
    }
}
