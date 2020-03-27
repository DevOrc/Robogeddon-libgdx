package com.noahcharlton.robogeddon.content;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.world.World;

public class RobotEntity extends Entity {

    public RobotEntity(World world) {
        super(EntityType.robotEntity, world);

        angularVelocity = 4f;
    }

    public static class RobotEntityType extends EntityType {

        private Texture texture;

        @Override
        public Entity create(World world) {
            return new RobotEntity(world);
        }

        @Override
        public void initRenderer() {
            texture = new Texture("robot.png");
        }

        @Override
        public void render(SpriteBatch batch, Entity entity) {
            GraphicsUtil.drawRotated(batch, texture, entity.getX(), entity.getY(), entity.getAngle());
        }

        @Override
        public String getTypeID() {
            return "EntityRobot";
        }
    }
}
