package com.noahcharlton.robogeddon.entity.drone;

import com.badlogic.gdx.math.Vector2;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.entity.BulletEntity;
import com.noahcharlton.robogeddon.entity.CustomEntityMessage;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.world.Chunk;
import com.noahcharlton.robogeddon.world.HasWorldPosition;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

public class AttackDroneEntity extends AbstractDroneEntity {

    private static final int RADIUS = DroneType.RADIUS;
    private static final int SHOOT_TIME = 30;
    private static final int MAX_VEL = 4;
    private static final int SHOOT_RANGE = (int) Math.pow(500, 2);

    private HasWorldPosition target;

    private int time;
    private boolean onTarget;

    public AttackDroneEntity(EntityType type, World world, Team team) {
        super(type, world, team);

        y = -150;
        angle = (float) (Math.PI / 2);
    }

    @Override
    protected void update() {
        if(target == null || !target.isWorldPositionValid()) {
            velocity = 0;
            angularVelocity = 0;
            time--;

            if(time < 0){
                findTarget();
                time = 30;
            }
            return;
        }

        Vector2 vecBetween = createVectorBetween(target);
        updateVelocity(vecBetween);
        updateAngle(vecBetween);
        updateShooter();
    }

    private void updateShooter() {
        if(world.isClient() || !onTarget || target == null){
            return;
        }

        if(time > 0) {
            time--;
        } else if(this.createVectorBetween(target).len2() < SHOOT_RANGE) {
            time = SHOOT_TIME;
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

        Server.runLater(() -> world.addEntity(bullet));
    }

    private void updateVelocity(Vector2 vecBetween) {
        var distance = 200 * 200;
        if(vecBetween.len2() > distance) {
            velocity = Math.min(velocity + .1f, MAX_VEL);
        } else {
            velocity *= .9;
        }
    }

    private void updateAngle(Vector2 vecBetween) {
        var angle = getAngle() - vecBetween.angleRad();

        while(angle < 0) {
            angle += Math.PI * 2;
        }
        while(angle > Math.PI * 2) {
            angle -= Math.PI * 2;
        }

        if(angle < .1 || angle > Math.PI * 2 - .1) {
            angularVelocity = 0;
            onTarget = true;
            return;
        }
        onTarget = false;

        if(angle > Math.PI) {
            angularVelocity = 0.04f;
        } else {
            angularVelocity = -.04f;
        }
    }

    @Side(Side.SERVER)
    private void findTarget() {
        if(world.isClient() || this.getTeam() == Team.NEUTRAL)
            return;

        float currentTargetDistanceSqr = SHOOT_RANGE;
        HasWorldPosition target = null;

        for(Entity entity : world.getEntities()) {
            float distanceSquared = createVectorBetween(entity).len2();
            if(entity.getTeam() != this.getTeam() && distanceSquared < currentTargetDistanceSqr) {

                target = entity;
                currentTargetDistanceSqr = distanceSquared;
            }
        }

        if(target == null){
            for(int x = getTile().getX() - Chunk.SIZE / 4; x <= getTile().getX() + Chunk.SIZE / 4; x++) {
                for(int y = getTile().getY() - Chunk.SIZE / 4; y <= getTile().getY() + Chunk.SIZE / 4; y++) {
                    var tile = world.getTileAt(x, y);
                    var distanceSquared = createVectorBetween(tile).len2();

                    if(distanceSquared < currentTargetDistanceSqr
                            && tile.getChunk().getTeam() != this.getTeam()
                            && tile.hasBlock()){
                        target = tile;
                        currentTargetDistanceSqr = distanceSquared;
                        System.out.println("Shotting at tile");
                    }
                }
            }
        }

        if(target != null){
            this.target = target;
            world.sendMessageToClient(new TargetFoundMessage(getId(), target));
        }
    }

    @Override
    public void onCustomMessageReceived(CustomEntityMessage message) {
        if(message instanceof TargetFoundMessage && world.isClient()) {
            target = ((TargetFoundMessage) message).getTarget(world);
            Log.debug("Drone Found Target: " + target.toString());
        } else {
            super.onCustomMessageReceived(message);
        }
    }

    static class TargetFoundMessage extends CustomEntityMessage {

        public final int v1;
        public final int v2;

        public TargetFoundMessage(int droneID, HasWorldPosition target) {
            super(droneID);

            if(target instanceof Tile){
                var tile = (Tile) target;
                v1 = tile.getX();
                v2 = tile.getY();
            }else if(target instanceof Entity){
                v1 = ((Entity) target).getId();
                v2 = Integer.MIN_VALUE;
            }else{
                throw new IllegalArgumentException("Invalid target type: " + target.getClass().getName());
            }
        }

        public HasWorldPosition getTarget(World world) {
            if(v2 == Integer.MIN_VALUE)
                return world.getEntityByID(v1);

            return world.getTileAt(v1, v2);
        }
    }
}
