package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.world.World;

public class Entity {

    public static final int DEFAULT_ID = -1;

    private final EntityType type;
    private final World world;

    private int id = DEFAULT_ID;
    private float x;
    private float y;
    private float angle;
    private float velocity;

    private boolean isDead;
    private boolean dirty;

    public Entity(EntityType type, World world) {
        this.type = type;
        this.world = world;
    }

    public final void fixedUpdate(){
        update();
        updateKinematics();
    }

    public void update(){}

    public void updateKinematics(){}

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setId(int id) {
        if(this.id != DEFAULT_ID){
            throw new IllegalStateException("This entity already has an ID: " + this.id);
        }
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public EntityType getType() {
        return type;
    }
}
