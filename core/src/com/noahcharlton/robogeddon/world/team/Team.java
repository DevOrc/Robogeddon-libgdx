package com.noahcharlton.robogeddon.world.team;

import com.badlogic.gdx.graphics.Color;

public enum Team {


    RED(Color.RED),
    BLUE(Color.BLUE),
    NEUTRAL(Color.GRAY);

    private final Color color;

    Team(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Team(" + color + ")";
    }

    public Color getColor() {
        return color;
    }
}
