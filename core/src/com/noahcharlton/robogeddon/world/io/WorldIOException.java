package com.noahcharlton.robogeddon.world.io;

import com.badlogic.gdx.utils.GdxRuntimeException;

public class WorldIOException extends GdxRuntimeException {

    public WorldIOException(String message) {
        super(message);
    }

    public WorldIOException(Throwable t) {
        super(t);
    }

    public WorldIOException(String message, Throwable t) {
        super(message, t);
    }
}
