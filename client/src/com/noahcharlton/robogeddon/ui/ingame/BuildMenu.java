package com.noahcharlton.robogeddon.ui.ingame;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.Block;
import com.noahcharlton.robogeddon.block.BlockGroup;
import com.noahcharlton.robogeddon.input.BuildBlock;
import com.noahcharlton.robogeddon.ui.background.ColorBackground;
import com.noahcharlton.robogeddon.ui.widget.IconButton;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class BuildMenu extends Widget{

    private static final int HEIGHT = 200;
    private static final int SUB_MENU_WIDTH = 140;

    private final Stack iconStack;

    public BuildMenu() {
        iconStack = new Stack();
        iconStack.setBackground(new ColorBackground());
        iconStack.setMinHeight(HEIGHT);

        for(BlockGroup group: Core.blockGroups.values()){
            var subMenu = createSubMenu(group);
            var iconButton = new IconButton(group.getIcon());
            iconButton.setOnClick((event, button) -> onIconClicked(subMenu));

            iconStack.add(iconButton);
            add(subMenu);
        }

        add(iconStack);
    }

    private void onIconClicked(Stack subMenu) {
        if(subMenu.isVisible()){
            subMenu.setVisible(false);
            return;
        }

        for(Widget child: getChildren()){
            if(child != iconStack){
                child.setVisible(false);
            }
        }
        subMenu.setVisible(true);
    }

    @Override
    public void layout() {
        for(Widget child: getChildren()){
            child.setMinHeight(HEIGHT);

            if(child != iconStack){
                child.setX(iconStack.getWidth() + iconStack.getX());
            }
        }

        setHeight(HEIGHT);
        setWidth(iconStack.getWidth() + SUB_MENU_WIDTH);
    }

    private Stack createSubMenu(BlockGroup group) {
        var menu = new Stack();
        menu.setBackground(new ColorBackground());
        menu.setWidth(SUB_MENU_WIDTH);
        menu.setVisible(false);

        for(Block block : group.getBlocks()){
            var button = new TextButton(block.getTypeID())
                    .setOnClick((event, b) -> startBuilding(block))
                    .setWidth(SUB_MENU_WIDTH - 10)
                    .pad().left(5).right(5);

            menu.add(button);
        }

        return menu;
    }

    private void startBuilding(Block block) {
        client.getProcessor().setBuildAction(new BuildBlock(block));
    }
}
