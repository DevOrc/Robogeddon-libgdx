package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;

public class ClientLauncher {

	public static void main (String[] arg) {
		Log.info("Client Launcher started!");
		Log.info("Core Version: " + Core.VERSION);
		Core.preInit();

		startClient();
	}

	private static void startClient() {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		config.foregroundFPS = 0;
		config.backgroundFPS = 120;
		config.vSyncEnabled = false;

		new LwjglApplication(GameClient.getInstance(), config);
	}
}
