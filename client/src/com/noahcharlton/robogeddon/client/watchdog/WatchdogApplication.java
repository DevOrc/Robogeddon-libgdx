package com.noahcharlton.robogeddon.client.watchdog;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class WatchdogApplication extends LwjglApplication {

    public WatchdogApplication(ApplicationListener listener, LwjglApplicationConfiguration config) {
        super(listener, config);

        Watchdog.watch(mainLoopThread);
    }
}
