package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

public class RepairDroneEntity extends Entity {

    public RepairDroneEntity(EntityType type, World world, Team team) {
        super(type, world, team);

        angle = (float) (Math.PI / 2f);
    }
}
