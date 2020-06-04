package com.noahcharlton.robogeddon.client;

import com.noahcharlton.robogeddon.util.log.Log;
import com.noahcharlton.robogeddon.world.settings.WorldSettings;

public class MainMenuServer extends LocalServer {

    public MainMenuServer(WorldSettings worldSettings) {
        super(worldSettings);
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Main Menu Server");

        while(true){ //This thread always runs
            super.run();

            Log.info("Pausing main menu world thread until continued");
            hibernate();
        }
    }

    private void hibernate() {
        while(!Thread.interrupted()){
            try{
                Thread.sleep(25);
            }catch(InterruptedException e){
                break;
            }
        }

        Log.info("Main menu world thread woken up!");
    }

    @Override
    public String getName() {
        return "Main Menu Local Server";
    }
}
