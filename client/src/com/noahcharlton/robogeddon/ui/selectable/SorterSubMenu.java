package com.noahcharlton.robogeddon.ui.selectable;

import com.badlogic.gdx.utils.Array;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.gate.SorterSelectionMessage;
import com.noahcharlton.robogeddon.block.gate.SorterTileEntity;
import com.noahcharlton.robogeddon.block.tileentity.inventory.ItemBuffer;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.ui.widget.Widget;
import com.noahcharlton.robogeddon.world.item.Item;

public class SorterSubMenu extends SelectableSubMenu {

    private final Stack stack = new Stack();

    public SorterSubMenu(String id) {
        super(id);

        stack.add(new Label("Selected:\n Outputs East/West").pad().all(5).bottom(10));
        Core.items.values().forEach(item -> stack.add(
                new ItemButton(item).setOnClick(this::onItemClick)));
        add(stack.pad().all(10));
    }

    private void onItemClick(ClickEvent clickEvent, Button button) {
        Array<Item> items = new Array<>();

        button.setSelected(!button.isSelected());
        for(Widget widget : stack.getChildren()){
            if(!(widget instanceof ItemButton))
                continue;

            var itemButton = (ItemButton) widget;

            if(itemButton.isSelected()){
                items.add(itemButton.getItem());
            }
        }

        var message = new SorterSelectionMessage(getTileEntity().getRootTile(), items.toArray(Item.class));
        client.getWorld().sendMessageToServer(message);
    }

    @Override
    public void update() {
        var tileEntity = (SorterTileEntity) getTileEntity();

        if(tileEntity == null)
            return;

        for(Widget widget : stack.getChildren()){
            if(!(widget instanceof ItemButton))
                continue;

            var itemButton = (ItemButton) widget;

            if(isSelected(tileEntity.getBuffers(), itemButton.getItem())){
                itemButton.setSelected(true);
            }else{
                itemButton.setSelected(false);
            }
        }
    }

    private boolean isSelected(ItemBuffer[] buffers, Item item) {
        for(ItemBuffer buffer : buffers){

            if(item.equals(buffer.currentItem())){
                return true;
            }
        }

        return false;
    }
}
