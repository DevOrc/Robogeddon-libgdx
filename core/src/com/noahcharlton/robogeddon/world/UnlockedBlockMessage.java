package com.noahcharlton.robogeddon.world;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.message.Message;

public class UnlockedBlockMessage implements Message {

    private final String blockID;

    public UnlockedBlockMessage(Block block) {
        this.blockID = block.getTypeID();
    }

    public Block getBlock() {
        return Core.blocks.get(blockID);
    }
}
