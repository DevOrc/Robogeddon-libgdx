package com.noahcharlton.robogeddon.world.io;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.util.GameData;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.ServerWorld;

@Side(Side.SERVER)
public class WorldIO {

    public static void save(ServerWorld world){
        var saveFile = GameData.getSaveFile("save");
        Log.info("Saving world: " + saveFile);

        var xml = new XmlWriter(saveFile).element("SaveGame");

        Core.saveGameHandlers.values().forEach(handler -> save(handler, xml, world));

        xml.pop();
        xml.flush();
        xml.close();
    }

    private static void save(WorldIOHandler handler, XmlWriter xml, ServerWorld world) {
        xml.element(handler.getTypeID());
        handler.save(xml, world);
        xml.pop();
    }

}
