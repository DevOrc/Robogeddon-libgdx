package com.noahcharlton.robogeddon.settings;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SerializationException;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.util.log.Log;
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
        Element element;

        try{
            element = new XmlReader().parse(settingsFile.readString(String.valueOf(StandardCharsets.UTF_8)));
        }catch(GdxRuntimeException | SerializationException e){
            Log.error("Failed to parse settings", e);
            Log.error("Due to error creating new settings file!");
            save();
            return;
        }

        load(element);
    }

    private static void load(Element element) {
        boolean failed = false;

        for(Setting setting : Core.settings.values()){
            try{
                setting.load(element);
            }catch(RuntimeException e){
                Log.warn("Failed to load setting " + setting.getName(), e);
                failed = true;
            }
        }

        if(failed){
            Log.warn("Due to error creating new settings file!");
            save();
        }
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
