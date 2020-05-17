package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.message.Message;

public class EntityUpdateMessage implements Message {

    private final int id;
    private final float x;
    private final float y;
    private final float angle;
    private final float velocity;
    private final float angularVelocity;
    private final float health;

    public EntityUpdateMessage(int id, float x, float y, float angle, float velocity, float angularVelocity, float health) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.velocity = velocity;
        this.angularVelocity = angularVelocity;
        this.health = health;
    }

    public EntityUpdateMessage(EntityUpdateMessage message) {
        this.id = message.id;
        this.x = message.x;
        this.y = message.y;
        this.angle = message.angle;
        this.velocity = message.velocity;
        this.angularVelocity = message.angularVelocity;
        this.health = message.health;
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

    public float getHealth() {
        return health;
    }
}
