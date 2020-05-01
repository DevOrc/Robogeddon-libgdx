package com.noahcharlton.robogeddon.util;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.noahcharlton.robogeddon.Core;

public class MiscTextures {

    private static Array<TextureRegion> brokenBlock;

    public static void init(){
        Core.assets.registerTextureGroup("blocks/break/stage").setOnLoad(t -> brokenBlock = t);
    }

    public static TextureRegion getBrokenBlock(float blockHealth){
        if(blockHealth >= 1){
            return null;
        }else if(blockHealth > .65){
            return brokenBlock.get(0);
        }else if(blockHealth > .35){
            return brokenBlock.get(1);
        }else if(blockHealth > .15){
            return brokenBlock.get(2);
        }else{
            return brokenBlock.get(3);
        }
    }
}
