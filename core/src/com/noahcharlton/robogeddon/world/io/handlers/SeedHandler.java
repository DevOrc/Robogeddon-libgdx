package com.noahcharlton.robogeddon.world.io.handlers;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.gen.WorldGenerator;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.WorldIOHandler;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

public class SeedHandler implements WorldIOHandler {

    @Override
    public void save(XmlWriter xml, ServerWorld world) {
        xml.element("Seed", world.getGenerator().getSeed());
    }

    @Override
    public void load(Element xml, ServerWorld world) {
        long seed = Long.parseLong(xml.getChildByName("Seed").getText());

        Log.debug("Seed: " + seed);
        world.setGenerator(new WorldGenerator(seed));
    }

    @Override
    public String getTypeID() {
        return "Seed";
    }
}
