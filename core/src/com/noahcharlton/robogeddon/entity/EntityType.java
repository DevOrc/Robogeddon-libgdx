package com.noahcharlton.robogeddon.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.entity.drone.AttackDroneEntity;
import com.noahcharlton.robogeddon.entity.drone.DroneType;
import com.noahcharlton.robogeddon.entity.drone.RepairDroneEntity;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.World;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityType implements HasID {

    public static final EntityType robotEntity = new RobotEntity.RobotEntityType();
    public static final EntityType bulletEntity = new BulletEntity.BulletEntityType();
    public static final EntityType attackDroneEntity =
            new DroneType<>(AttackDroneEntity.class, "attack", "Attack", 15, 5 * 60);
    public static final EntityType repairDroneEntity =
            new DroneType<>(RepairDroneEntity.class, "repair", "Repair", 25, 8 * 60);

    private static final List<DroneType> drones = new ArrayList<>();

    public abstract Entity create(World world, Team team);

    @Side(Side.BOTH)
    public static void preInit(){
        Core.entities.registerAll(robotEntity, bulletEntity, attackDroneEntity, repairDroneEntity);

        for(EntityType type : Core.entities.values()){
            if(type instanceof DroneType)
                drones.add((DroneType) type);
        }
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

    public static List<DroneType> getDrones() {
        return drones;
    }
}
