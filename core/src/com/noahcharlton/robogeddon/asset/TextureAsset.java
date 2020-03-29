package com.noahcharlton.robogeddon.asset;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Log;

public class TextureAsset extends Asset<TextureRegion> {

    private final String path;

    public TextureAsset(String path) {
        this.path = path;

        if(path.contains(".png") || path.contains(".jpg")){
            Log.warn("Textures should not have paths: " + path);
        }
    }

    @Override
    protected TextureRegion load() {
        return AssetManager.textures.findRegion(path);
    }

    @Override
    protected String getName() {
        return "Texture(" + path + ")";
    }

    @Override
    protected void dispose() {
        //No need to dispose each texture, the atlas will dispose all of them
    }
}
