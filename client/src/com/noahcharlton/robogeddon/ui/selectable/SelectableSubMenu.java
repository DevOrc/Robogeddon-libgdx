package com.noahcharlton.robogeddon.ui.selectable;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Widget;
import com.noahcharlton.robogeddon.world.Tile;

public class SelectableSubMenu extends Widget {

    public SelectableSubMenu() {
        setBackground(new NinePatchBackground(UIAssets.selectableMenu));
        setSize(250, 250);
    }

    @Override
    public void layout() {
        var width = getMinWidth();
        var height = getMinHeight();

        for(Widget child : getChildren()) {
            child.setX(getX());
            child.setY(getY());

            width = Math.max(width, child.getWidth());
            height = Math.max(height, child.getHeight());
        }

        setWidth(width);
        setHeight(height);
    }

    @Override
    protected void onClick(ClickEvent event) {
        float width = 14;
        float height = 12;
        float x = getX() + getWidth() - width;
        float y = getY() + getHeight() - height;

        if(event.getX() > x && event.getY() > y && event.getX() < x + width && event.getY() < y + height){
            client.getProcessor().setSelectable(null);
        }
    }

    protected TileEntity getTileEntity(){
        if(client.getProcessor().getSelectable() == null)
            return null;

        return ((Tile) client.getProcessor().getSelectable()).getTileEntity();
    }
}
