package com.noahcharlton.robogeddon.ui.help;

import com.badlogic.gdx.utils.Align;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.util.help.BlockHelpInfo;
import com.noahcharlton.robogeddon.util.help.HelpInfo;

public class BlockHelpPage extends HelpScene {

    public BlockHelpPage(HelpInfo info) {
        super(info);

        var blockInfo = (BlockHelpInfo) info;
        var block = blockInfo.getBlock();

        var title = new Label(block.getDisplayName()).setFont(UIAssets.largeFont);
        var desc = createDescription(block, blockInfo);

        add(title.pad().top(40)).align(Align.top);
        add(new Label(desc).setWrap(true).setMaxWidth(600));
    }

    private String createDescription(Block block, BlockHelpInfo blockInfo) {
        StringBuilder builder = new StringBuilder(blockInfo.getDesc());
        builder.append("\n\n");

        builder.append("Hardness: ").append(block.getHardness()).append("\n");
        builder.append("Size: ").append(block.getWidth()).append("x").append(block.getHeight()).append("\n");
        builder.append("Block ID: ").append(block.getTypeID()).append("\n");

        if(!block.getRequirements().isEmpty()){
            builder.append("\n\nRequirements: \n");

            block.getRequirements().forEach(requirement ->
                    builder.append(requirement.getDisplayInfo()).append("\n"));
        }

        return builder.toString();
    }
}
