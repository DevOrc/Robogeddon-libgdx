package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.entity.collision.HasCollision;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.World;

public class BulletEntity extends Entity {

    private static final float RADIUS = BulletEntityType.RADIUS;
    private static final float VELOCITY = 28;

    @Side(Side.SERVER)
    private int dirtyCount = 0;
    @Side(Side.SERVER)
    private Entity shooter;

    public BulletEntity(World world) {
        super(EntityType.bulletEntity, world);

        this.setVelocity(VELOCITY);
    }

    @Override
    protected void update() {
        if(world.isClient())
            return;

        for(Entity entity: world.getEntities()){
            if(entity instanceof HasCollision && entity != shooter){
                if(collided(entity)){
                    this.setDead(true);
                    entity.damage(3);
                    return;
                }
            }
        }
    }

    private boolean collided(Entity other) {
        var vec = createVectorBetween(other);
        var radius = ((HasCollision) other).getRadius() + RADIUS;
        return vec.len2() < (radius * radius);
    }

    @Override
    public void setDirty(boolean dirty) {
        if(dirtyCount ++ > 30){
            setDirty(true);
            dirtyCount = 0;
        }
    }

    @Override
    public boolean isDead() {
        return (world.isServer() && !isInWorld()) || super.isDead();
    }

    public void setShooter(Entity shooter) {
        this.shooter = shooter;
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
