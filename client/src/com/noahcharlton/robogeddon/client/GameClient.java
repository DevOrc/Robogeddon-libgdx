package com.noahcharlton.robogeddon.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.noahcharlton.robogeddon.Log;

public class GameClient extends ApplicationAdapter {
    @Override
    public void create() {
        Thread.currentThread().setName("Client");
        Log.info("Game Client created!");
    }

    @Override
    public void render() {

    }

    @Override
    public void resize(int width, int height) {
        Log.debug("Resize: (" + width + ", " + height + ")");
    }

    @Override
    public void dispose() {
        Log.info("Game Client disposed!");
    }
}
