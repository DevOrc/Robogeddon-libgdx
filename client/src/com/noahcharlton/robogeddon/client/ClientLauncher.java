package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;

import java.util.Arrays;

public class ClientLauncher {

	public static void main (String[] args) {
		Log.info("Client Launcher started!");
		Log.info("Core Version: " + Core.VERSION + "-" + Core.VERSION_TYPE);
		Log.info("Args: " + Arrays.toString(args));

		Core.client = GameClient.getInstance();
		Core.preInit();

		startClient();
	}

	private static void startClient() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		config.foregroundFPS = 0;
		config.backgroundFPS = 120;
		config.vSyncEnabled = true;
		config.title = "Robogeddon " + Core.VERSION + "-" + Core.VERSION_TYPE;

		new LwjglApplication(GameClient.getInstance(), config);
	}
}
