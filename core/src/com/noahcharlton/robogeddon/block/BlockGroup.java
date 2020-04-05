package com.noahcharlton.robogeddon.block;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.util.Side;

@Side(Side.CLIENT)
public class BlockGroup implements HasID {

    private final Block[] blocks;
    private final String id;
    private TextureRegion icon;

    public BlockGroup(String id, Block... blocks) {
        this.blocks = blocks;
        this.id = id;
        Core.assets.registerTexture("blocks/groups/" + id).setOnLoad(i -> icon = i);
    }


    public Block[] getBlocks() {
        return blocks;
    }

    public TextureRegion getIcon() {
        return icon;
    }

    @Override
    public String getTypeID() {
        return id;
    }
}
