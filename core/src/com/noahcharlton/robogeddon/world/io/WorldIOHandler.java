package com.noahcharlton.robogeddon.world.io;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.io.handlers.ChunkIOHandler;
import com.noahcharlton.robogeddon.world.io.handlers.EntityIOHandler;
import com.noahcharlton.robogeddon.world.io.handlers.InventoryIOHandler;
import com.noahcharlton.robogeddon.world.io.handlers.VersionIOHandler;

public interface WorldIOHandler extends HasID {

    static void preInit() {
        Core.saveGameHandlers.register(new VersionIOHandler());
        Core.saveGameHandlers.register(new ChunkIOHandler());
        Core.saveGameHandlers.register(new InventoryIOHandler());
        Core.saveGameHandlers.register(new EntityIOHandler());
    }

    void save(XmlWriter xml, ServerWorld world);

    void load(Element xml, ServerWorld world);
}
