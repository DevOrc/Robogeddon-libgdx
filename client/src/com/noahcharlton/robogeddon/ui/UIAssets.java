package com.noahcharlton.robogeddon.ui;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.noahcharlton.robogeddon.Core;

public class UIAssets {

    public static NinePatch button;

    public static void init(){
        Core.assets.registerNinePatch("ui/button").setOnLoad(b -> button = b);
    }
}
