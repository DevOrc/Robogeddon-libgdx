package com.noahcharlton.robogeddon.block;

import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.entity.Entity;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.util.help.BlockHelpInfo;
import com.noahcharlton.robogeddon.util.help.HelpInfo;
import com.noahcharlton.robogeddon.util.help.HelpInfoLoader;
import com.noahcharlton.robogeddon.world.Tile;
import com.noahcharlton.robogeddon.world.item.ItemStack;
import com.noahcharlton.robogeddon.world.team.Team;

import java.util.Collections;
import java.util.List;

public abstract class Block implements HasID {

    private final String id;

    @Side(Side.CLIENT)
    protected BlockRenderer renderer;
    @Side(Side.CLIENT)
    protected HelpInfo helpInfo;

    protected List<Block> requiredBlocks = Collections.emptyList();
    protected List<ItemStack> requirements = Collections.emptyList();

    public Block(String id) {
        this.id = id;
    }

    protected void preInit(){}

    @Side(Side.CLIENT)
    protected void initRenderer(){
        renderer = new DefaultBlockRenderer(this);
    }

    @Side(Side.CLIENT)
    public void init(){
        initRenderer();
        createHelpInfo();
    }

    @Side(Side.CLIENT)
    protected void createHelpInfo() {
        helpInfo = HelpInfoLoader.getEntry(id);

        if(helpInfo == null){ //If there is no special help info, auto generate one
            helpInfo = new BlockHelpInfo(this);
        }
    }

    @Side(Side.CLIENT)
    public String[] getDescriptionParameters(){
        return new String[]{};
    }

    @Side(Side.SERVER)
    public boolean canBuildAt(Tile tile, Entity placer){
        var chunkTeam = tile.getChunk().getTeam();

        return chunkTeam == placer.getTeam() || chunkTeam == Team.NEUTRAL;
    }

    public final List<ItemStack> getRequirements(){
        return requirements;
    }

    public final List<Block> getRequiredBlocks(){
        return requiredBlocks;
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

    /**
     * How resistant a block is to damage
     * (higher does less damage per hit)
     */
    public float getHardness(){
        return 1f;
    }

    public BlockRenderer getRenderer() {
        return renderer;
    }

    public HelpInfo getHelpInfo() {
        return helpInfo;
    }

    public String getTypeID() {
        return id;
    }
}
