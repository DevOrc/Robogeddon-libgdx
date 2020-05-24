package com.noahcharlton.robogeddon.entity.drone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.collision.HasCollision;
import com.noahcharlton.robogeddon.util.MiscAssets;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

public abstract class AbstractDroneEntity extends Entity implements HasCollision {

    private ParticleEffectPool.PooledEffect effect;

    public AbstractDroneEntity(EntityType type, World world, Team team) {
        super(type, world, team);

        this.effect = MiscAssets.obtainEngineFlame();
        this.effect.start();
    }

    void renderEffect(SpriteBatch batch){
        if(effect != null && isEngineOn()){
            var angleDeg = 180 + (float) (angle * 180 / Math.PI);

            for(ParticleEmitter emitter : effect.getEmitters()){
                emitter.getAngle().setHigh(angleDeg - 60, angleDeg + 60);
                emitter.getAngle().setLow(angleDeg - 60, angleDeg + 60);
            }

            var x = getX() - (float) (4 * Math.cos(angle));
            var y = getY() - (float) (4 * Math.sin(angle));

            effect.setPosition(x, y);
            effect.update(Gdx.graphics.getDeltaTime());
            effect.draw(batch);
        }
    }

    public boolean isEngineOn(){
        return velocity > .2f;
    }

    @Override
    public void setDead(boolean dead) {
        super.setDead(dead);

        if(dead){
            effect.free();
            effect = null;
        }
    }

    public void customRender(SpriteBatch batch){}

    @Override
    public float getRadius() {
        return DroneType.RADIUS;
    }
}
