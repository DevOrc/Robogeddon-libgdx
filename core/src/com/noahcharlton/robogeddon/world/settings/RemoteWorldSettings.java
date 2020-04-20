package com.noahcharlton.robogeddon.world.settings;

public class RemoteWorldSettings implements WorldSettings {

    private final String ip;

    public RemoteWorldSettings(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }
}
