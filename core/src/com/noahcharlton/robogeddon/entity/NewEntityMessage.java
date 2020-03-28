package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.message.Message;

public class NewEntityMessage implements Message {

    private final String entityType;
    private final int ID;

    public NewEntityMessage(String entityType, int ID) {
        this.entityType = entityType;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getEntityType() {
        return entityType;
    }
}
