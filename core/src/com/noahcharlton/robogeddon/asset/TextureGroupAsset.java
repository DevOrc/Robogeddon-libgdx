package com.noahcharlton.robogeddon.asset;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.noahcharlton.robogeddon.util.log.Log;

public class TextureGroupAsset extends Asset<Array<TextureRegion>> {

    private final String path;

    public TextureGroupAsset(String path) {
        this.path = path;

        if(path.contains(".png") || path.contains(".jpg")){
            Log.warn("Textures should not have paths: " + path);
        }
    }

    @Override
    protected Array<TextureRegion> load() {
        var textures = new Array<TextureRegion>();
        AssetManager.textures.findRegions(path).forEach(textures::add);
        
        return textures;
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
