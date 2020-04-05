package com.noahcharlton.robogeddon.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;

public class UIAssets {

    public static NinePatch button;
    public static NinePatch buttonHover;
    public static BitmapFont smallFont;
    public static BitmapFont titleFont;
    public static TextureRegion iconButton;
    public static TextureRegion iconButtonHover;

    public static void init(){
        Core.assets.registerNinePatch("ui/button").setOnLoad(b -> button = b);
        Core.assets.registerNinePatch("ui/buttonHover").setOnLoad(b -> buttonHover = b);
        Core.assets.registerBitmapFont("small").setOnLoad(font -> smallFont = font);
        Core.assets.registerBitmapFont("title").setOnLoad(font -> titleFont = font);
        Core.assets.registerTexture("ui/iconButton").setOnLoad(t -> iconButton = t);
        Core.assets.registerTexture("ui/iconButtonHover").setOnLoad(t -> iconButtonHover = t);
    }
}
