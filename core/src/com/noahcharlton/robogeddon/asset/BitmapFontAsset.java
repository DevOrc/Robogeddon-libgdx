package com.noahcharlton.robogeddon.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class BitmapFontAsset extends Asset<BitmapFont> {

    private final String path;
    private BitmapFont font;

    public BitmapFontAsset(String name) {
        this.path = "fonts/" + name;
    }

    @Override
    protected String getName() {
        return "BitmapFont(" + path + ")";
    }

    @Override
    protected BitmapFont load() {
        font = new BitmapFont(Gdx.files.internal(path + ".fnt"));
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        return font;
    }

    @Override
    protected void dispose() {
        if(font != null)
            font.dispose();
    }
}
