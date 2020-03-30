package com.noahcharlton.robogeddon.ui.ingame;

import com.badlogic.gdx.Gdx;
import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.PairLayout;
import com.noahcharlton.robogeddon.ui.widget.TextureWidget;
import com.noahcharlton.robogeddon.ui.widget.Widget;
import com.noahcharlton.robogeddon.world.item.Item;

public class InventoryList extends Widget {

    public InventoryList() {
        for(Item item: Core.items.values()){
            var texture = new TextureWidget().setTexture(item.getTexture());
            var label = new Label().setSupplier(() -> getText(item)).setFont(UIAssets.small).pad().bottom(10);
            var pair = new PairLayout().setLeft(texture).setRight(label);

            add(pair);
        }
    }

    private String getText(Item item) {
        int amount = 0;

        if(client.getWorld() != null){
            amount = client.getWorld().getInventoryForItem(item);
        }

        return item.getTypeID() + ": " + amount;
    }

    @Override
    public void layout() {
        float x = getX() + 10;
        float y = Gdx.graphics.getHeight() - 40;

        for(Widget child: getChildren()){
            child.setX(x);
            child.setY(y);

            y -= 40;
        }
    }
}
