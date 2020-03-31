package com.noahcharlton.robogeddon.input;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.noahcharlton.robogeddon.world.Tile;

public interface BuildAction {

    String getName();

    void onClick(Tile tile, int button);

    default void render(SpriteBatch batch){}

}

