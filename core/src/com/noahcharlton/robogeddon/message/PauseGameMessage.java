package com.noahcharlton.robogeddon.message;

public class PauseGameMessage implements Message {

    private final boolean paused;

    public PauseGameMessage(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }
}
