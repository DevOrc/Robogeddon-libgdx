package com.noahcharlton.robogeddon.entity.drone;

import com.badlogic.gdx.math.Vector2;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.entity.BulletEntity;
import com.noahcharlton.robogeddon.entity.CustomEntityMessage;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.RobotEntity;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

public class AttackDroneEntity extends AbstractDroneEntity {

    private static final int RADIUS = DroneType.RADIUS;
    private static final int SHOOT_TIME = 30;
    private static final int MAX_VEL = 4;
    private static final int SHOOT_RANGE = (int) Math.pow(500, 2);

    private Entity target;

    private int shooterTime;
    private boolean onTarget;

    public AttackDroneEntity(EntityType type, World world, Team team) {
        super(type, world, team);

        y = -400;
        angle = (float) (Math.PI / 2);
    }

    @Override
    protected void update() {
        if(target == null || target.isDead()){
            velocity = 0;
            angularVelocity = 0;

            findTarget();
            return;
        }

        Vector2 vecBetween = createVectorBetween(target);
        updateVelocity(vecBetween);
        updateAngle(vecBetween);
        updateShooter();
    }

    private void updateShooter() {
        if(world.isClient() || !onTarget || target == null || target.isDead())
            return;

        if(shooterTime > 0){
            shooterTime--;
        }else if(this.createVectorBetween(target).len2() < SHOOT_RANGE){
            shooterTime = SHOOT_TIME;
            shoot();
        }
    }

    @Side(Side.SERVER)
    private void shoot() {
        ServerWorld world = (ServerWorld) this.world;
        BulletEntity bullet = (BulletEntity) EntityType.bulletEntity.create(world, team);

        bullet.setX((float) (getX() + (RADIUS * Math.cos(angle))));
        bullet.setY((float) (getY() + (RADIUS * Math.sin(angle))));
        bullet.setAngle(angle);
        bullet.setIgnoreBlocks(true);

        Server.runLater(() -> world.addEntity(bullet));
    }

    private void updateVelocity(Vector2 vecBetween) {
        var distance = 200 * 200;
        if(vecBetween.len2() > distance){
            velocity = Math.min(velocity + .1f, MAX_VEL);
        }else{
            velocity *= .9;
        }
    }

    private void updateAngle(Vector2 vecBetween) {
        var angle = getAngle() - vecBetween.angleRad();

        while(angle < 0){
            angle += Math.PI * 2;
        }
        while(angle > Math.PI * 2){
            angle -= Math.PI * 2;
        }

        if(angle < .1 || angle > Math.PI * 2 - .1){
            angularVelocity = 0;
            onTarget = true;
            return;
        }
        onTarget = false;

        if(angle > Math.PI){
            angularVelocity = 0.04f;
        }else{
            angularVelocity = -.04f;
        }
    }

    @Side(Side.SERVER)
    private void findTarget() {
        if(world.isClient())
            return;

        for(Entity entity : world.getEntities()){
            if(entity instanceof RobotEntity){
                target = entity;
                world.sendMessageToClient(new TargetFoundMessage(getId(), target.getId()));
                Log.debug("Drone Found Target: " + entity.getId());
                return;
            }
        }
    }

    @Override
    public void onCustomMessageReceived(CustomEntityMessage message) {
        if(message instanceof TargetFoundMessage && world.isClient()) {
            int targetID = ((TargetFoundMessage) message).targetID;
            target = world.getEntityByID(targetID);
            Log.debug("Drone Found Target: " + targetID);
        } else {
            super.onCustomMessageReceived(message);
        }
    }

    static class TargetFoundMessage extends CustomEntityMessage{

        public final int targetID;

        public TargetFoundMessage(int droneID, int targetID) {
            super(droneID);

            this.targetID = targetID;
        }
    }
}
