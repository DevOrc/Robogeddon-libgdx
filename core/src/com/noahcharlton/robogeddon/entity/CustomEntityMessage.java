package com.noahcharlton.robogeddon.entity;

import com.noahcharlton.robogeddon.message.Message;

public class CustomEntityMessage implements Message {

    private final int ID;

    public CustomEntityMessage(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }
}
