package com.noahcharlton.robogeddon.settings;

import com.badlogic.gdx.files.FileHandle;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.util.GameData;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlReader;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

import java.nio.charset.StandardCharsets;

public class SettingsIO {

    public static void load(){
        FileHandle settingsFile = GameData.getSettingsFile();

        if(settingsFile.exists()){
            Log.debug("Loading settings...");
            load(settingsFile);
        }else{
            Log.warn("No settings file found. Saving default settings!");
            save();
        }
    }

    private static void load(FileHandle settingsFile) {
        Element element = new XmlReader().parse(settingsFile.readString(String.valueOf(StandardCharsets.UTF_8)));

        Core.settings.values().forEach(setting -> setting.load(element));

    }

    public static void save(){
        Log.debug("Saving settings!");
        FileHandle settingsFile = GameData.getSettingsFile();
        XmlWriter writer = new XmlWriter(settingsFile).element("Settings");

        Core.settings.values().forEach(setting -> setting.save(writer));
        writer.pop();
        writer.flush();
        writer.close();
    }

}
