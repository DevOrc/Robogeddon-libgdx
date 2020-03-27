package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.world.World;

public class TestEntity extends Entity {

    public TestEntity(World world) {
        super(EntityType.testEntity, world);
    }

    static class TestEntityType extends EntityType {
        @Override
        public Entity create(World world) {
            return new TestEntity(world);
        }

        @Override
        public String getTypeID() {
            return "TestEntity";
        }
    }


}
