package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.world.World;

public class BulletEntity extends Entity {

    private static final float VELOCITY = 28;

    public BulletEntity(World world) {
        super(EntityType.bulletEntity, world);

        this.setVelocity(VELOCITY);
    }

    @Override
    public boolean isDead() {
        return world.isServer() && !isInWorld();
    }

    static class BulletEntityType extends EntityType{

        private static final float RADIUS = 3;

        private TextureRegion texture;

        @Override
        public Entity create(World world) {
            return new BulletEntity(world);
        }

        @Override
        public void initRenderer() {
            Core.assets.registerTexture("entities/bullet").setOnLoad(sprite -> texture = sprite);
        }

        @Override
        public void render(SpriteBatch batch, Entity entity) {
            if(entity.isInWorld())
                batch.draw(texture, entity.getX() - RADIUS, entity.getY() - RADIUS);
        }

        @Override
        public String getTypeID() {
            return "bullet";
        }
    }

}
