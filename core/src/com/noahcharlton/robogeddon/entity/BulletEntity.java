package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.beacon.BeaconBlock;
import com.noahcharlton.robogeddon.entity.collision.HasCollision;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.stream.Stream;

public class BulletEntity extends Entity {

    private static final float RADIUS = BulletEntityType.RADIUS;
    private static final float VELOCITY = 20;

    @Side(Side.SERVER)
    private boolean ignoreBlocks;
    @Side(Side.SERVER)
    private int dirtyCount = 0;

    public BulletEntity(World world, Team team) {
        super(EntityType.bulletEntity, world, team);

        this.setVelocity(VELOCITY);
    }

    @Override
    protected void update() {
        if(world.isClient())
            return;

        for(Entity entity: world.getEntities()){
            if(entity instanceof HasCollision && entity.getTeam() != team){
                if(collided(entity)){
                    this.setDead(true);
                    entity.damage(3);
                    return;
                }
            }
        }

        var tile = world.getTileAt((int) x / Tile.SIZE, (int) y / Tile.SIZE);

        //Check neighbors because the bullet can sometimes glitch through blocks if its hit at an angle
        Stream.of(tile.getNeighbors()).filter(this::isBlockInRange).forEach(this::checkBlockHit);
        checkBlockHit(tile);
    }

    private void checkBlockHit(Tile tile) {
        if(tile != null && tile.hasBlock() && canHitBlock(tile)){
            tile.damage();
            this.setDead(true);
        }
    }

    private boolean canHitBlock(Tile tile) {
        if(ignoreBlocks)
            return false;

        if(tile.getBlock() instanceof BeaconBlock)
            return true;

        return tile.getChunk().getTeam() != team;
    }

    private boolean isBlockInRange(Tile tile) {
        if(tile == null)
            return false;

        return new Vector2(tile.getWorldXPos(), tile.getWorldYPos())
                .sub(x, y).len2() < 50 * 50;
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

    @Side(Side.SERVER)
    public void setIgnoreBlocks(boolean ignoreBlocks) {
        this.ignoreBlocks = ignoreBlocks;
    }

    static class BulletEntityType extends EntityType{

        private static final float RADIUS = 3;

        private TextureRegion texture;

        @Override
        public Entity create(World world, Team team) {
            return new BulletEntity(world, team);
        }

        @Override
        public void initRenderer() {
            Core.assets.registerTexture("entities/bullet").setOnLoad(sprite -> texture = sprite);
        }

        @Override
        public void render(SpriteBatch batch, Entity entity) {
            var tile = entity.getTile();

            if(tile != null && tile.hasBlock()){
                return;
            }

            if(entity.isInWorld())
                batch.draw(texture, entity.getX() - RADIUS, entity.getY() - RADIUS);
        }

        @Override
        public String getTypeID() {
            return "bullet";
        }

        @Override
        public boolean isTargetable() {
            return false;
        }
    }

}
