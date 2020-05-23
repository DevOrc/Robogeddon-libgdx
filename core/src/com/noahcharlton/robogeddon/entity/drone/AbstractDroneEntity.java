package com.noahcharlton.robogeddon.entity.drone;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

public abstract class AbstractDroneEntity extends Entity {

    public AbstractDroneEntity(EntityType type, World world, Team team) {
        super(type, world, team);
    }

    public void customRender(SpriteBatch batch){}
}
