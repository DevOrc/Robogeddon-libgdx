package com.noahcharlton.robogeddon.asset;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.noahcharlton.robogeddon.util.Side;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

@Side(Side.CLIENT)
public class AssetManager {

    private ArrayDeque<Asset> unloadedAssets = new ArrayDeque<>();
    private List<Asset> assets = new ArrayList<>();

    static TextureAtlas textures;

    public AssetManager(){
        var atlas = new AtlasAsset();
        atlas.setOnLoad(a -> textures = a);

        registerAsset(atlas);
    }

    public void reload() {
        dispose();
        unloadedAssets.addAll(assets);
    }

    public TextureAsset registerTexture(String path) {
        var asset = new TextureAsset(path);
        registerAsset(asset);

        return asset;
    }

    public NinePatchAsset registerNinePatch(String path) {
        var asset = new NinePatchAsset(path);
        registerAsset(asset);

        return asset;
    }

    public BitmapFontAsset registerBitmapFont(String name) {
        var asset = new BitmapFontAsset(name);
        registerAsset(asset);

        return asset;
    }

    public void update() {
        Asset nextAsset = unloadedAssets.poll();

        if(nextAsset == null) {
            return;
        }

        nextAsset.onLoad();
    }

    public void registerAsset(Asset asset) {
        unloadedAssets.add(asset);
        assets.add(asset);
    }

    public double getPercentDone() {
        return 1.0 - ((double) unloadedAssets.size() / (double) assets.size());
    }

    public boolean isDone() {
        return unloadedAssets.isEmpty();
    }

    public void dispose() {
        assets.forEach(Asset::dispose);
    }

    public int getAssetCount() {
        return assets.size();
    }
}
