package com.noahcharlton.robogeddon.ui;

import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.ui.ingame.InGameScene;

public class WorldLoadingScene extends TextScene{

    private final long timeoutTime = System.currentTimeMillis() + 5000;

    public WorldLoadingScene() {
        backButton.setVisible(false);
    }

    @Override
    public void update() {
        if(client.getWorld().getServer().isConnected()){
            client.getUi().setScene(new InGameScene());
        }else if(timeoutTime < System.currentTimeMillis()){
            onWorldTimeout();
        }

        long dotTime = System.currentTimeMillis() % 1500;

        if(dotTime < 500){
            setText("Loading .");
        }else if(dotTime < 1000){
            setText("Loading ..");
        }else{
            setText("Loading ...");
        }

        getLabel().pack();
        invalidateParent();
    }

    private void onWorldTimeout() {
        Log.warn("World load timeout reached!");
        client.gotoMainMenu();
        client.getUi().setScene(new DisconnectedScene("World Load timeout reached!"));
    }
}