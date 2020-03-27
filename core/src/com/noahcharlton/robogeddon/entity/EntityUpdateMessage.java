package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.Message;

public class EntityUpdateMessage implements Message {

    private final int id;
    private final float x;
    private final float y;
    private final float angle;

    public EntityUpdateMessage(int id, float x, float y, float angle) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
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
}
