package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

public class TestEntity extends Entity {

    public TestEntity(World world, Team team) {
        super(EntityType.testEntity, world, team);
    }

    static class TestEntityType extends EntityType {
        @Override
        public Entity create(World world, Team team) {
            return new TestEntity(world, team);
        }

        @Override
        public String getTypeID() {
            return "TestEntity";
        }
    }


}
