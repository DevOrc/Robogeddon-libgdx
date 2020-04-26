package com.noahcharlton.robogeddon.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleEffectAsset extends Asset<ParticleEffect> {

    private final String path;
    private ParticleEffect effect;

    public ParticleEffectAsset(String name) {
        this.path = "particle effects/" + name;
    }

    @Override
    protected String getName() {
        return "BitmapFont(" + path + ")";
    }

    @Override
    protected ParticleEffect load() {
        effect =  new ParticleEffect();
        effect.load(Gdx.files.internal(path), AssetManager.textures);

        return effect;
    }

    @Override
    protected void dispose() {
        effect.dispose();
    }
}
