package com.noahcharlton.robogeddon.world.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.util.Side;

public class Item implements HasID {

    public static final int ICON_RADIUS = 5;
    private final String id;

    @Side(Side.CLIENT)
    private TextureRegion texture;
    @Side(Side.CLIENT)
    private TextureRegion tinyTexture;

    public Item(String id) {
        this.id = id;
    }

    @Side(Side.CLIENT)
    public void init(){
        var path = "items/" + id;
        Core.assets.registerTexture(path).setOnLoad(textureRegion -> texture = textureRegion);
        Core.assets.registerTexture(path + "_tiny").setOnLoad(textureRegion -> tinyTexture = textureRegion);
    }

    public ItemStack stack(int amount){
        return new ItemStack(this, amount);
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public TextureRegion getTinyTexture() {
        return tinyTexture;
    }

    @Override
    public String getTypeID() {
        return id;
    }
}
