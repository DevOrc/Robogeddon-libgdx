package com.noahcharlton.robogeddon.ui.selectable;

import com.noahcharlton.robogeddon.block.tileentity.TileEntity;
import com.noahcharlton.robogeddon.ui.UIAssets;
import com.noahcharlton.robogeddon.ui.background.NinePatchBackground;
import com.noahcharlton.robogeddon.ui.event.ClickEvent;
import com.noahcharlton.robogeddon.ui.widget.Widget;
import com.noahcharlton.robogeddon.util.Selectable;
import com.noahcharlton.robogeddon.world.Tile;

public class SelectableSubMenu extends Widget {

    private final String id;

    public SelectableSubMenu(String id) {
        this.id = id;

        setBackground(new NinePatchBackground(UIAssets.dialog));
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
        if(UIAssets.isEventOnDialogCloseButton(this, event)){
            client.getProcessor().setSelectable(null);
        }
    }



    protected TileEntity getTileEntity(){
        Selectable selectable = client.getProcessor().getSelectable();

        if(!(selectable instanceof Tile))
            return null;

        return ((Tile) selectable).getTileEntity();
    }

    public String getId() {
        return id;
    }
}
