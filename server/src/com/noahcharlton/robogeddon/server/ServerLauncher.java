package com.noahcharlton.robogeddon.server;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.Log;

public class ServerLauncher {

    public static void main(String[] args) {
        Log.info("Server Launcher started!");
        Log.info("Core Version: " + Core.VERSION);
    }
}
