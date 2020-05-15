package com.noahcharlton.robogeddon.util;

import com.badlogic.gdx.files.FileHandle;
import com.noahcharlton.robogeddon.util.log.Log;

public class GameData {

    private static final String directoryPath = System.getProperty("user.dir") + "/game_data";
    private static final FileHandle directory = new FileHandle(directoryPath);
    private static final FileHandle saveDirectory = directory.child("/saves");

    public static void init(){
        if(!directory.exists()){
            Log.info("Game data directory does not exist... Making a new one");
            directory.mkdirs();
        }else{
            Log.debug("Game data found: " + directory.toString());
        }
    }

    public static FileHandle getChild(String name){
        return directory.child(name);
    }

    public static FileHandle getSettingsFile(){
        return directory.child("settings.xml");
    }

    public static FileHandle getSaveFile(String name){
        return saveDirectory.child(name + ".xml");
    }

    public static FileHandle[] getSaveFiles() {
        return saveDirectory.list();
    }
}
