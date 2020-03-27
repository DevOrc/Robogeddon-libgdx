package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.world.World;

public abstract class EntityType implements HasID {

    public static final EntityType testEntity = new TestEntity.TestEntityType();

    public abstract Entity create(World world);

    public static void preInit(){
        Core.entities.register(testEntity);
    }
}
