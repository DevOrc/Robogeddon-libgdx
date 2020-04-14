package com.noahcharlton.robogeddon.world.io.handlers;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.WorldIOException;
import com.noahcharlton.robogeddon.world.io.WorldIOHandler;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

public class VersionIOHandler implements WorldIOHandler {

    @Override
    public void save(XmlWriter xml, ServerWorld world) {
        xml.element("Core", Core.VERSION);
        xml.element("Build-Type", Core.VERSION_TYPE);
    }

    @Override
    public void load(Element xml, ServerWorld world) {
        var saveVersion = xml.get("Core");
        var saveBuild = xml.get("Build-Type");

        if(!Core.VERSION.equals(saveVersion)){
            throw new WorldIOException("Version's do not match! Save Version: " + saveVersion +
                    ", Core Version:"  + Core.VERSION);
        }

        if(!Core.VERSION_TYPE.equals(saveBuild)){
            Log.warn("Builds do not match! Save Build: " + saveBuild + ", Core Build: " + Core.VERSION_TYPE);
        }
    }

    @Override
    public String getTypeID() {
        return "Version";
    }
}
