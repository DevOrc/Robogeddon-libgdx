package com.noahcharlton.robogeddon.world.floor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;

public class Floor implements HasID {

    private final String id;
    private final String displayName;

    @Side(Side.CLIENT)
    private TextureRegion texture;

    public Floor(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    @Side(Side.CLIENT)
    public void init(){
        var path = "floors/" + id;

        Core.assets.registerTexture(path).setOnLoad(textureRegion -> texture = textureRegion);
    }

    @Side(Side.CLIENT)
    public void render(SpriteBatch batch, Tile tile){
        float x = tile.getPixelX();
        float y = tile.getPixelY();

        batch.draw(texture, x, y);
    }

    public String getDisplayName(){
        return displayName;
    }

    @Override
    public String getTypeID() {
        return id;
    }
}
