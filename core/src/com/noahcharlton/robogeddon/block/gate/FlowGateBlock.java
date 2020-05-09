package com.noahcharlton.robogeddon.block.gate;

import com.noahcharlton.robogeddon.block.Block;

public class FlowGateBlock extends Block {

    public FlowGateBlock(String id) {
        super(id);
    }

    @Override
    public String getDisplayName() {
        return "Flow Gate";
    }
}
