package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.World;

public abstract class EntityType implements HasID {

    public static final EntityType testEntity = new TestEntity.TestEntityType();
    public static final EntityType robotEntity = new RobotEntity.RobotEntityType();

    public abstract Entity create(World world);

    @Side(Side.BOTH)
    public static void preInit(){
        Core.entities.register(testEntity);
        Core.entities.register(robotEntity);
    }

    @Side(Side.CLIENT)
    public static void init() {
        Core.entities.values().forEach(EntityType::initRenderer);
    }

    public void initRenderer(){}

    public void render(SpriteBatch batch, Entity entity){}
}
