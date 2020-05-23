package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.util.GraphicsUtil;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DroneType<T extends Entity> extends EntityType {

    static final int RADIUS = 32;

    private final String droneType;
    private final Constructor<T> constructor;
    private final int health;

    private TextureRegion offTexture;
    private TextureRegion onTexture;

    public DroneType(Class<T> clazz, String type, int health) {
        this.droneType = type;
        this.health = health;

        try {
            this.constructor = clazz.getConstructor(EntityType.class, World.class, Team.class);
        } catch(NoSuchMethodException e) {
            throw new GdxRuntimeException("Could not find proper constructor in class: " + clazz.getName());
        }
    }

    @Override
    public void initRenderer() {
        var basePath = "entities/" + getTypeID();
        Core.assets.registerTexture(basePath + "_off").setOnLoad(t -> offTexture = t);
        Core.assets.registerTexture(basePath + "_on").setOnLoad(t -> onTexture = t);
    }

    @Override
    public void render(SpriteBatch batch, Entity entity) {
        float angle = (float) (entity.getAngle() * 180 / Math.PI) - 90;
        float x = entity.getX() - RADIUS;
        float y = entity.getY() - RADIUS;
        TextureRegion texture = entity.velocity < .25 ? offTexture : onTexture;

        batch.setColor(0f ,0f, 0f, .2f);
        GraphicsUtil.drawRotated(batch, texture, x - 10, y - 10, angle);
        batch.setColor(1f ,1f, 1f, 1f);
        GraphicsUtil.drawRotated(batch, texture, x, y, angle);
        renderHealthBar(batch, entity, RADIUS);
    }

    @Override
    public Entity create(World world, Team team) {
        try {
            return constructor.newInstance(this, world, team);
        } catch(InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new GdxRuntimeException("Unable to create entity " + getTypeID(), e);
        }
    }

    @Override
    public String getTypeID() {
        return droneType + "_drone";
    }

    @Override
    public int getHealth() {
        return health;
    }
}
