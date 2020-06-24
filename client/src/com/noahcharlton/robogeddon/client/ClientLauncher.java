package com.noahcharlton.robogeddon.client;import com.badlogic.gdx.Files;import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;import com.noahcharlton.robogeddon.Core;import com.noahcharlton.robogeddon.client.watchdog.Watchdog;import com.noahcharlton.robogeddon.client.watchdog.WatchdogApplication;import com.noahcharlton.robogeddon.settings.ClientSettings;import com.noahcharlton.robogeddon.util.log.Log;import java.util.Arrays;public class ClientLauncher {	public static void main (String[] args) {		Log.init();		Log.info("Client Launcher started!");		Log.info("Core Version: " + Core.VERSION + "-" + Core.VERSION_TYPE);		Log.info("Args: " + Arrays.toString(args));		Watchdog.watch(Thread.currentThread());		ClientSettings.init();		Core.client = GameClient.getInstance();		Core.preInit();		Core.assets.addAtlasAsset();		startLwjgl();	}	private static void startLwjgl() {		var config = new Lwjgl3ApplicationConfiguration();		config.setIdleFPS(60);		config.setWindowedMode(ClientSettings.windowWidth.getValue(), ClientSettings.windowHeight.getValue());		config.useVsync(ClientSettings.vSync.getValue());		config.setTitle("Robogeddon " + Core.VERSION + "-" + Core.VERSION_TYPE);		config.setWindowIcon(Files.FileType.Internal,				"icons/icon128.png",				"icons/icon64.png",				"icons/icon32.png");		new WatchdogApplication(GameClient.getInstance(), config);	}}