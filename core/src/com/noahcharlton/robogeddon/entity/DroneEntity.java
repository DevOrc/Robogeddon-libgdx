package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.Server;
import com.noahcharlton.robogeddon.entity.collision.HasCollision;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.World;

public class DroneEntity extends Entity implements HasCollision {

    private static final int RADIUS = DroneEntityType.RADIUS;
    private static final int SHOOT_TIME = 30;
    private static final int MAX_VEL = 4;

    private Entity target;

    private int shooterTime;
    private boolean onTarget;

    public DroneEntity(World world) {
        super(EntityType.droneEntity, world);

        x = world.getPixelWidth() / 2f;
        y = world.getPixelHeight() / 2f - 400;
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
        }else{
            shooterTime = SHOOT_TIME;
            shoot();
        }
    }

    @Side(Side.SERVER)
    private void shoot() {
        ServerWorld world = (ServerWorld) this.world;
        BulletEntity bullet = (BulletEntity) EntityType.bulletEntity.create(world);
        bullet.setShooter(this);

        bullet.setX((float) (getX() + (RADIUS * Math.cos(angle))));
        bullet.setY((float) (getY() + (RADIUS * Math.sin(angle))));
        bullet.setAngle(angle);

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
        if(message instanceof TargetFoundMessage && world.isClient()){
            int targetID = ((TargetFoundMessage) message).targetID;
            target = world.getEntityByID(targetID);
            Log.debug("Drone Found Target: " + targetID);
        }else{
            super.onCustomMessageReceived(message);
        }
    }

    @Override
    public float getRadius() {
        return RADIUS;
    }

    public static class DroneEntityType extends EntityType {

        static final int RADIUS = 32;

        private TextureRegion offTexture;
        private TextureRegion onTexture;

        @Override
        public void initRenderer() {
            Core.assets.registerTexture("entities/drone_off").setOnLoad(t -> offTexture = t);
            Core.assets.registerTexture("entities/drone_on").setOnLoad(t -> onTexture = t);
        }

        @Override
        public void render(SpriteBatch batch, Entity entity) {
            float angle = (float) (entity.getAngle() * 180 / Math.PI) - 90;
            float x = entity.getX() - RADIUS;
            float y = entity.getY() - RADIUS;
            TextureRegion texture = entity.velocity < .25 ? offTexture : onTexture;

            GraphicsUtil.drawRotated(batch, texture, x, y, angle);
            renderHealthBar(batch, entity, RADIUS);
        }

        @Override
        public Entity create(World world) {
            return new DroneEntity(world);
        }

        @Override
        public String getTypeID() {
            return "drone";
        }

        @Override
        public int getHealth() {
            return 15;
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
