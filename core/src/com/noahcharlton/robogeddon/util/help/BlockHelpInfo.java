package com.noahcharlton.robogeddon.util.help;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.world.io.Element;

public class BlockHelpInfo implements HelpInfo{

    public static final String guiID = "block_help_info";

    private final Block block;
    private final String desc;

    public BlockHelpInfo(Element entry) {
        block = Core.blocks.get(entry.getAttribute("id"));
        var desc = HelpInfoLoader.escapeDescriptionString(entry.get("Desc"));
        var parameters = block.getDescriptionParameters();

        for(int i = 0; i < parameters.length; i++) {
            desc = desc.replace("{" + i + "}", parameters[i]);
        }

        this.desc = desc;
    }

    public BlockHelpInfo(Block block) {
        this.block = block;
        this.desc = "";
    }

    public Block getBlock() {
        return block;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String getGuiID() {
        return guiID;
    }
}
