package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.beacon.BeaconBlock;
import com.noahcharlton.robogeddon.entity.collision.HasCollision;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

public class BulletEntity extends Entity {

    private static final float RADIUS = BulletEntityType.RADIUS;
    private static final float VELOCITY = 20;
    public static final float ENTITY_DAMAGE = 3f;

    @Side(Side.SERVER)
    private boolean ignoreBlocks;
    @Side(Side.SERVER)
    private int dirtyCount = 0;

    private float height = 120;
    private float heightVelocity;

    public BulletEntity(World world, Team team) {
        super(EntityType.bulletEntity, world, team);

        this.setVelocity(VELOCITY);
    }

    @Override
    public EntityUpdateMessage createUpdateMessage() {
        return new BulletUpdateMessage(super.createUpdateMessage(), height);
    }

    @Override
    public void onUpdateMessage(EntityUpdateMessage message) {
        super.onUpdateMessage(message);

        height = ((BulletUpdateMessage) message).height;
    }

    @Override
    protected void update() {
        heightVelocity += .1f;
        height -= heightVelocity;
        if(world.isClient())
            return;

        if(height <= 0){
            setDead(true);
        }

        for(Entity entity: world.getEntities()){
            if(entity instanceof HasCollision && entity.getTeam() != team){
                if(collided(entity)){
                    this.setDead(true);
                    entity.damage(ENTITY_DAMAGE);
                    return;
                }
            }
        }


        var tileHit = hasHitBlock();
        if(tileHit != null){
            setDead(true);
            tileHit.damage();
        }
    }

    private Tile hasHitBlock() {
        var tile = world.getTileAt((int) x / Tile.SIZE, (int) y / Tile.SIZE);

        if(tile == null)
            return null;

        if(checkBlockHit(tile)){
            return tile;
        }

        for(Tile neighbor : tile.getNeighbors()){
            if(checkBlockHit(neighbor) && isBlockInRange(neighbor))
                return neighbor;
        }

        return null;
    }

    private boolean checkBlockHit(Tile tile) {
        if(tile != null && tile.hasBlock() && canHitBlock(tile)){
            return true;
        }

        return false;
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
                .sub(x, y).len2() < 20 * 20;
    }

    private boolean collided(Entity other) {
        var vec = createVectorBetween(other);
        var radius = ((HasCollision) other).getRadius() + RADIUS;
        return vec.len2() < (radius * radius);
    }

    @Override
    public void setDirty(boolean dirty) {
        if(dirtyCount++ > 30){
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

    public void setHeight(float height) {
        this.height = height;
    }

    static class BulletUpdateMessage extends EntityUpdateMessage {

        float height;

        public BulletUpdateMessage(EntityUpdateMessage message, float height) {
            super(message);

            this.height = height;
        }
    }

    static class BulletEntityType extends EntityType{

        private static final float RADIUS = 8;

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
            BulletEntity bullet = (BulletEntity) entity;
            var tile = bullet.getTile();

            if(tile != null && bullet.hasHitBlock() != null){
                return;
            }

            if(entity.isInWorld()){
                var x = entity.getX() - RADIUS;
                var y =  entity.getY() - RADIUS;
                var angle = (float) (entity.getAngle() * 180 / Math.PI);
                var shadowHeight = (bullet.height / 8f) + 2;

                GraphicsUtil.drawRotated(batch, texture, x, y, angle);
                batch.setColor(0f, 0f, 0f, .45f);
                GraphicsUtil.drawRotated(batch, texture, x - shadowHeight, y - shadowHeight, angle);
            }

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
