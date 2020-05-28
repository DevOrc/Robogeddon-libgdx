package com.noahcharlton.robogeddon.util.help;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.world.io.Element;

public class BlockHelpInfo implements HelpInfo{

    public static final String guiID = "block_help_info";

    private final Block block;
    private final String desc;
    private final String simpleDesc;

    public BlockHelpInfo(Element entry) {
        if(entry.hasAttribute("block_id")){
            block = Core.blocks.getOrNull(entry.getAttribute("block_id"));;
        }else{
            block = Core.blocks.getOrNull(entry.getAttribute("id"));
        }

        var desc = HelpInfoLoader.escapeDescriptionString(entry.get("Desc"));
        var simpleDesc = HelpInfoLoader.escapeDescriptionString(entry.get("SimpleDesc", ""));

        this.simpleDesc = replaceDescriptionParameters(simpleDesc);
        this.desc = replaceDescriptionParameters(desc);
    }

    private String replaceDescriptionParameters(String desc) {
        var parameters = block.getDescriptionParameters();

        for(int i = 0; i < parameters.length; i++) {
            desc = desc.replace("{" + i + "}", parameters[i]);
        }
        return desc;
    }

    public BlockHelpInfo(Block block) {
        this.block = block;
        this.desc = "";
        this.simpleDesc = "";
    }

    public Block getBlock() {
        return block;
    }

    public String getDesc() {
        return desc;
    }

    public String getSimpleDesc() {
        return simpleDesc;
    }

    @Override
    public String getGuiID() {
        return guiID;
    }
}
