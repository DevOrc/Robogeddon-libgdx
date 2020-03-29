package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;

import java.util.Arrays;

public class ClientLauncher {

	public static boolean runLocal = true;

	public static void main (String[] args) {
		Log.info("Client Launcher started!");
		Log.info("Core Version: " + Core.VERSION);
		Log.info("Args: " + Arrays.toString(args));
		checkArgs(args);

		Core.client = GameClient.getInstance();
		Core.preInit();

		startClient();
	}

	private static void checkArgs(String[] args) {
		for(String arg : args) {
			if(arg.equals("--remote")){
				runLocal = false;
				Log.info("--remote found, setting to remote server mode");
				return;
			}
		}

		Log.info("Setting to local server mode");
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
