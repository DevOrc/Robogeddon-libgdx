package com.noahcharlton.robogeddon.asset;

import com.noahcharlton.robogeddon.Log;

import java.util.function.Consumer;

public abstract class Asset<T> {

    private Consumer<T> onLoad = x -> {
    };

    final void onLoad() {
        Log.debug("Loading asset: " + getName());
        var asset = load();

        if(asset == null)
            throw new RuntimeException("Failed to find asset: " + getName());

        onLoad.accept(asset);
    }

    protected abstract String getName();

    protected abstract T load();

    protected abstract void dispose();

    public void setOnLoad(Consumer<T> onLoad) {
        this.onLoad = onLoad;
    }
}
