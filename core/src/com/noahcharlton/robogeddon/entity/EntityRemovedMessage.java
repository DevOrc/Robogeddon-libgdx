package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.message.Message;

public class EntityRemovedMessage implements Message {

    private final int ID;

    public EntityRemovedMessage(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
