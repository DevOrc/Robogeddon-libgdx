package com.noahcharlton.robogeddon.ui.selectable;

import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.TextureWidget;
import com.noahcharlton.robogeddon.world.item.Item;

public class ItemButton extends Button {

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

    public Item getItem() {
        return item;
    }
}