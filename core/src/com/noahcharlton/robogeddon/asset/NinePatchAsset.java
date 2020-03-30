package com.noahcharlton.robogeddon.asset;

import com.badlogic.gdx.graphics.g2d.NinePatch;

public class NinePatchAsset extends Asset<NinePatch> {

    private final String path;

    public NinePatchAsset(String path) {
        this.path = path;
    }

    @Override
    protected NinePatch load() {
        return AssetManager.textures.createPatch(path);
    }

    @Override
    protected String getName() {
        return "NinePatch(" + path + ")";
    }

    @Override
    protected void dispose() {
        //No need to dispose each texture, the atlas will dispose all of them
    }
}
