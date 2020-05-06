package com.noahcharlton.robogeddon.settings;import com.badlogic.gdx.Gdx;import com.badlogic.gdx.Input;import com.noahcharlton.robogeddon.Core;import com.noahcharlton.robogeddon.util.log.Log;/** * This class is responsible for registering the settings * the client needs */public class ClientSettings {    public static final BooleanSetting fullscreen = new BooleanSetting("Fullscreen", false, ClientSettings::fullscreen);    public static final BooleanSetting vSync = new BooleanSetting("VSync", false, ClientSettings::vSync);    public static final KeySetting keyForward = new KeySetting("Move Forward", Input.Keys.W);    public static final KeySetting keyReverse = new KeySetting("Move Back", Input.Keys.S);    public static final KeySetting keyLeft = new KeySetting("Move Left", Input.Keys.A);    public static final KeySetting keyRight = new KeySetting("Move Right", Input.Keys.D);    public static final KeySetting keyShoot = new KeySetting("Shoot", Input.Keys.SPACE);    public static final KeySetting keyPause = new KeySetting("Pause Game", Input.Keys.ESCAPE);    public static final KeySetting keyRotateBlock = new KeySetting("Rotate Block", Input.Keys.Q);    public static final IntSetting windowWidth = new IntSetting("WindowWidth", 900);    public static final IntSetting windowHeight = new IntSetting("WindowHeight", 700);    public static void init(){        windowWidth.setOnSettingsScreen(false);        windowHeight.setOnSettingsScreen(false);        Core.settings.registerAll(windowWidth, windowHeight, fullscreen, vSync, keyForward, keyReverse, keyLeft,                keyRight, keyShoot, keyPause, keyRotateBlock);    }    private static void vSync(boolean value) {        Gdx.graphics.setVSync(value);    }    private static void fullscreen(boolean value) {        if(value) {            Log.info("Setting fullscreen!");            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());        } else {            Gdx.graphics.setWindowedMode(ClientSettings.windowWidth.getValue(), ClientSettings.windowHeight.getValue());        }    }}