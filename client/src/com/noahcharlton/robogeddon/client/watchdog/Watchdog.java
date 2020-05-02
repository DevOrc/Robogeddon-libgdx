package com.noahcharlton.robogeddon.client.watchdog;

import com.badlogic.gdx.Gdx;
import com.noahcharlton.robogeddon.Log;

import javax.swing.*;

public class Watchdog implements Thread.UncaughtExceptionHandler {

    private static final int MAX_STACKTRACE_LENGTH = 2000;

    public static void watch(Thread thread){
        Watchdog watchdog = new Watchdog();

        thread.setUncaughtExceptionHandler(watchdog);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if(Gdx.app != null)
            Gdx.app.exit();

        Log.error("Game threw exception! Exiting app...");
        e.printStackTrace();

        SwingUtilities.invokeLater(() -> showErrorMessage(e));
    }

    private void showErrorMessage(Throwable error) {
        Log.debug("Showing error dialog!");

        new ErrorDialog(error).setVisible(true);
    }
}