package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.message.Message;

public class EntityUpdateMessage implements Message {

    private final int id;
    private final float x;
    private final float y;
    private final float angle;
    private final float velocity;
    private final float angularVelocity;
    private final int health;

    public EntityUpdateMessage(int id, float x, float y, float angle, float velocity, float angularVelocity, int health) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.velocity = velocity;
        this.angularVelocity = angularVelocity;
        this.health = health;
    }

    public int getId() {
        return id;
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }

    public float getAngle() {
        return angle;
    }

    public float getVelocity() {
        return velocity;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public int getHealth() {
        return health;
    }
}
