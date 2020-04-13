package com.noahcharlton.robogeddon.ui.selectable;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.block.portal.UnloaderSetMessage;
import com.noahcharlton.robogeddon.block.portal.UnloaderTileEntity;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.*;
import com.noahcharlton.robogeddon.world.item.Item;

public class UnloaderSubMenu extends SelectableSubMenu {

    private final Stack stack = new Stack();

    public UnloaderSubMenu() {
        Core.items.values().forEach(item -> stack.add(new ItemButton(item)));
        add(stack.pad().all(5));
    }

    private class ItemButton extends Button {

       private final Item item;
       private final TextureWidget texture;
       private final Label label;

       public ItemButton(Item item) {
           this.item = item;
           this.texture = add(new TextureWidget()).setTexture(item.getTexture());
           this.label = add(new Label()).setText(item.getDisplayName()).setFont(UIAssets.smallFont);

           setDefaultBackground(new NinePatchBackground(UIAssets.button));
           setOnHover(new NinePatchBackground(UIAssets.buttonHover));
           setSelectedBackground(new NinePatchBackground(UIAssets.buttonSelected));
       }

       @Override
       public void layout() {
           var x = getX() + 10;
           var y = getY() + 10;

           texture.setPosition(x, y);
           x += texture.getWidth() + 10;
           label.setPosition(x, y + 10);
           x += label.getMinWidth() + 10;

           setWidth(Math.min(200, x + 25));
           setHeight(20 + texture.getHeight());
       }

        @Override
        protected void onClick(ClickEvent event) {
            var message = new UnloaderSetMessage(getTileEntity().getRootTile(), item);

            client.getWorld().sendMessageToServer(message);
        }

        public Item getItem() {
            return item;
        }
    }

    @Override
    public void update() {
        var tileEntity = (UnloaderTileEntity) getTileEntity();

        if(tileEntity == null)
            return;

        for(Widget widget : stack.getChildren()){
            var itemButton = (ItemButton) widget;

            if(itemButton.getItem() == tileEntity.getBuffers()[0].currentItem()){
                itemButton.setSelected(true);
            }else{
                itemButton.setSelected(false);
            }
        }
    }
}