package com.noahcharlton.robogeddon.content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.entity.CustomEntityMessage;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.world.World;

public class RobotEntity extends Entity {

    private static final float MAX_VELOCITY = 7;
    private static final float MAX_ANGULAR_VELOCITY = .1f;

    private boolean wKey;
    private boolean aKey;
    private boolean sKey;
    private boolean dKey;

    public RobotEntity(World world) {
        super(EntityType.robotEntity, world);

        angle = (float) (Math.PI / 2);
    }

    @Override
    public void update() {
        if(world.isClient())
            sendInputValues();

        updateControls();
        trimVelocity();
    }

    private void trimVelocity() {
        if(velocity > MAX_VELOCITY){
            velocity = MAX_VELOCITY;
        }else if(velocity < 0){
            velocity = 0;
        }

        if(Math.abs(angularVelocity) > MAX_ANGULAR_VELOCITY){
            angularVelocity = Math.signum(angularVelocity) * MAX_ANGULAR_VELOCITY;
        }
    }

    private void updateControls() {
        if(wKey){
            velocity += .8f;
        }else if(sKey){
            velocity -= 1.2f;
        }else{
            velocity -= .25f;
        }

        if(dKey){
            angularVelocity += -.01f;
        }else if(aKey){
            angularVelocity += .05f;
        }else{
            angularVelocity *= .85;
        }
    }

    private void sendInputValues() {
        boolean w = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean a = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean s = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean d = Gdx.input.isKeyPressed(Input.Keys.D);

        if(wKey != w || a != aKey || s != sKey || d != dKey){
            var message = new RobotInputMessage(getId(), w, a, s, d);
            wKey = w;
            aKey = a;
            sKey = s;
            dKey = d;

            world.sendMessageToServer(message);
            Log.trace("Sending robot controls to server!");
        }
    }

    @Override
    public void onCustomMessageReceived(CustomEntityMessage message) {
        if(message instanceof RobotInputMessage && world.isServer()){
            var input = (RobotInputMessage) message;
            wKey = input.wKey;
            aKey = input.aKey;
            sKey = input.sKey;
            dKey = input.dKey;
            Log.debug("Updated robot controls!");
        }else{
            super.onCustomMessageReceived(message);
        }
    }

    public static class RobotEntityType extends EntityType {

        private Texture onTexture;
        private Texture offTexture;

        @Override
        public Entity create(World world) {
            return new RobotEntity(world);
        }

        @Override
        public void initRenderer() {
            onTexture = new Texture("robot_on.png");
            offTexture = new Texture("robot_off.png");
        }

        @Override
        public void render(SpriteBatch batch, Entity entity) {
            if(!(entity instanceof RobotEntity)){
                throw new UnsupportedOperationException();
            }

            var texture = ((RobotEntity) entity).wKey ? onTexture : offTexture;
            float angle = (float) (entity.getAngle() * 180 / Math.PI) - 90;
            GraphicsUtil.drawRotated(batch, texture, entity.getX(), entity.getY(), angle);
        }

        @Override
        public String getTypeID() {
            return "EntityRobot";
        }
    }

    public static class RobotInputMessage extends CustomEntityMessage{

        final boolean wKey;
        final boolean aKey;
        final boolean sKey;
        final boolean dKey;

        public RobotInputMessage(int ID, boolean wKey, boolean aKey, boolean sKey, boolean dKey) {
            super(ID);
            this.wKey = wKey;
            this.aKey = aKey;
            this.sKey = sKey;
            this.dKey = dKey;
        }
    }
}
