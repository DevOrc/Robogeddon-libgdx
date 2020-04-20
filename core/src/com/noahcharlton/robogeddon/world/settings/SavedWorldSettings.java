package com.noahcharlton.robogeddon.world.settings;

import com.badlogic.gdx.files.FileHandle;
import com.noahcharlton.robogeddon.util.GameData;

public class SavedWorldSettings implements WorldSettings {

    private final String name;

    public SavedWorldSettings(String name) {
        this.name = name;
    }

    public FileHandle getFile() {
        return GameData.getSaveFile(name);
    }

    @Override
    public String toString() {
        return "SavedWorld(" + name + ")";
    }
}
