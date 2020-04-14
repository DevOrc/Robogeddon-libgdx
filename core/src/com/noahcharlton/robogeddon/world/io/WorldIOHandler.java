package com.noahcharlton.robogeddon.world.io;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.world.ServerWorld;

public interface WorldIOHandler extends HasID {

    static void preInit() {
        Core.saveGameHandlers.register(new VersionIOHandler());
    }

    void save(XmlWriter writer, ServerWorld world);

    void load(XmlWriter writer, ServerWorld world);
}
