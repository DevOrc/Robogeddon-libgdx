package com.noahcharlton.robogeddon.world.io.handlers;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.WorldIOHandler;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

public class UnlockedBlockHandler implements WorldIOHandler {

    @Override
    public void save(XmlWriter xml, ServerWorld world) {
        for(Block block : world.getUnlockedBlocks())
            xml.element("Block", block.getTypeID());
    }

    @Override
    public void load(Element xml, ServerWorld world) {
        for(Element child : xml.getChildrenByName("Block"))
            world.getUnlockedBlocks().add(Core.blocks.get(child.getText()));
    }

    @Override
    public String getTypeID() {
        return "UnlockedBlocks";
    }
}
