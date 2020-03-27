package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;

public class ClientLauncher {

	public static void main (String[] arg) {
		Log.info("Client Launcher started!");
		Log.info("Core Version: " + Core.VERSION);

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.forceExit = false;
		new LwjglApplication(new GameClient(), config);
	}
}
