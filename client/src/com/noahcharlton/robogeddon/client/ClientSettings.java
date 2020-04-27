package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.Gdx;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.settings.BooleanSetting;

/**
 * This class is responsible for registering the settings
 * the client needs
 */
public class ClientSettings {

    static void init(){
        Core.settings.register(new BooleanSetting("VSync", false, ClientSettings::vSync));
        Core.settings.register(new BooleanSetting("Fullscreen", false, ClientSettings::fullscreen));
    }

    private static void vSync(boolean value) {
        Gdx.graphics.setVSync(value);
    }

    private static void fullscreen(boolean value) {
        if(value) {
            Log.info("Setting fullscreen!");
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(ClientLauncher.WINDOW_WIDTH, ClientLauncher.WINDOW_HEIGHT);
        }
    }
}
