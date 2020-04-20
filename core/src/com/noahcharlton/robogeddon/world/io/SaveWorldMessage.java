package com.noahcharlton.robogeddon.world.io;

import com.noahcharlton.robogeddon.message.Message;

public class SaveWorldMessage implements Message {

    private final String path;

    public SaveWorldMessage(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
