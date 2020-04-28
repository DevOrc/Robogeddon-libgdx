package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.client.watchdog.WatchdogApplication;

import java.util.Arrays;

public class ClientLauncher {

	public static final int WINDOW_WIDTH = 640;
	public static final int WINDOW_HEIGHT = 480;

	public static void main (String[] args) {
		Log.info("Client Launcher started!");
		Log.info("Core Version: " + Core.VERSION + "-" + Core.VERSION_TYPE);
		Log.info("Args: " + Arrays.toString(args));
		ClientSettings.init();

		Core.client = GameClient.getInstance();
		Core.preInit();

		startLwjgl();
	}

	private static void startLwjgl() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		config.foregroundFPS = 0;
		config.width = WINDOW_WIDTH;
		config.height = WINDOW_HEIGHT;
		config.backgroundFPS = 120;
		config.vSyncEnabled = false;
		config.title = "Robogeddon " + Core.VERSION + "-" + Core.VERSION_TYPE;

		new WatchdogApplication(GameClient.getInstance(), config);
	}
}
