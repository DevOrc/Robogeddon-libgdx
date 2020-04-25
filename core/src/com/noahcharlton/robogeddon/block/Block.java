package com.noahcharlton.robogeddon.block;

import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.Collections;
import java.util.List;

public abstract class Block implements HasID {

    private final String id;

    @Side(Side.CLIENT)
    protected BlockRenderer renderer;

    public Block(String id) {
        this.id = id;
    }

    @Side(Side.CLIENT)
    public void initRenderer(){
        renderer = new DefaultBlockRenderer(this);
    }

    @Side(Side.SERVER)
    public boolean canBuildAt(Tile tile, Entity placer){
        var chunkTeam = tile.getChunk().getTeam();

        return chunkTeam == placer.getTeam() || chunkTeam == Team.NEUTRAL;
    }

    public List<ItemStack> getRequirements(){
        return Collections.emptyList();
    }

    public String getDisplayName(){
        return getTypeID();
    }

    public int getWidth(){
        return 1;
    }

    public int getHeight(){
        return 1;
    }

    public BlockRenderer getRenderer() {
        return renderer;
    }

    public String getTypeID() {
        return id;
    }
}
