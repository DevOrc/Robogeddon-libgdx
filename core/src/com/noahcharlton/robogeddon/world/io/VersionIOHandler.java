package com.noahcharlton.robogeddon.world.io;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.world.ServerWorld;

public class VersionIOHandler implements WorldIOHandler{

    @Override
    public void save(XmlWriter writer, ServerWorld world) {
        writer.element("Core", Core.VERSION);
        writer.element("Build-Type", Core.VERSION_TYPE);
    }

    @Override
    public void load(XmlWriter writer, ServerWorld world) {

    }

    @Override
    public String getTypeID() {
        return "Version";
    }
}
