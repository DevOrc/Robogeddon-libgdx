package com.noahcharlton.robogeddon.world.io;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.util.GameData;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.settings.SavedWorldSettings;

import java.nio.charset.StandardCharsets;

@Side(Side.SERVER)
public class WorldIO {

    public static void save(ServerWorld world, String path){
        if(path == null || path.isBlank())
            return;

        var saveFile = GameData.getSaveFile(path);
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

    public static void load(ServerWorld world, SavedWorldSettings settings){
        var file = settings.getFile();
        Log.info("loading world: " + file);

        var reader = new XmlReader().parse(file.readString(String.valueOf(StandardCharsets.UTF_8)));

        Core.saveGameHandlers.values().forEach(handler -> load(handler, reader, world));
    }

    private static void load(WorldIOHandler handler, Element reader, ServerWorld world) {
        var element = reader.getChildByName(handler.getTypeID());
        handler.load(element, world);
    }
}
