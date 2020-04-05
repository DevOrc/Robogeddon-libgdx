package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

public abstract class EntityType implements HasID {

    public static final EntityType testEntity = new TestEntity.TestEntityType();
    public static final EntityType robotEntity = new RobotEntity.RobotEntityType();
    public static final EntityType bulletEntity = new BulletEntity.BulletEntityType();
    public static final EntityType droneEntity = new DroneEntity.DroneEntityType();

    public abstract Entity create(World world, Team team);

    @Side(Side.BOTH)
    public static void preInit(){
        Core.entities.register(testEntity);
        Core.entities.register(robotEntity);
        Core.entities.register(bulletEntity);
        Core.entities.register(droneEntity);
    }

    @Side(Side.CLIENT)
    public static void init() { }

    public void initRenderer(){}

    public void render(SpriteBatch batch, Entity entity){}

    public void renderHealthBar(SpriteBatch batch, Entity entity, float radius){
        var startHealth = entity.getType().getHealth();

        if(startHealth == entity.getHealth()){
            return;
        }

        var shapeDrawer = Core.client.getGameShapeDrawer();
        var width = radius * 2;
        var x = entity.getX() - radius;
        var y = entity.getY() + radius + 10;
        var innerWidth = (width - 2f) * entity.getHealth() / startHealth;

        shapeDrawer.filledRectangle(x, y, width, 6, Color.WHITE);
        shapeDrawer.filledRectangle(x + 1f, y + 1f, innerWidth, 4, entity.getTeam().getColor());
    }

    public int getHealth(){
        return 1;
    }

    /**
     * @return if this entity type should be shot at by turrets
     */
    public boolean isTargetable(){
        return true;
    }
}
