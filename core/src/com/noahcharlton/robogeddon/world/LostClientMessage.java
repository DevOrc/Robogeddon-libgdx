package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.message.Message;

public class LostClientMessage implements Message {

    private final int connectionID;

    public LostClientMessage(int connectionID) {
        this.connectionID = connectionID;
    }

    public int getConnectionID() {
        return connectionID;
    }
}
