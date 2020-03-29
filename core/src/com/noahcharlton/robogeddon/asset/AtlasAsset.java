package com.noahcharlton.robogeddon.asset;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AtlasAsset extends Asset<TextureAtlas> {

    @Override
    protected TextureAtlas load() {
        return new TextureAtlas("sprites.atlas");
    }

    @Override
    protected void dispose() {
        AssetManager.textures.dispose();
    }

    @Override
    protected String getName() {
        return "TextureAtlas";
    }
}
