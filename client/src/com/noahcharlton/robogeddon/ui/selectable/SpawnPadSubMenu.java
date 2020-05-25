package com.noahcharlton.robogeddon.ui.selectable;

import com.noahcharlton.robogeddon.block.drone.SpawnPadTileEntity;
import com.noahcharlton.robogeddon.block.drone.SpawnPadUpdateMessage;
import com.noahcharlton.robogeddon.entity.EntityType;
import com.noahcharlton.robogeddon.entity.drone.DroneType;
import com.noahcharlton.robogeddon.ui.widget.Button;
import com.noahcharlton.robogeddon.ui.widget.Label;
import com.noahcharlton.robogeddon.ui.widget.Stack;
import com.noahcharlton.robogeddon.ui.widget.TextButton;
import com.noahcharlton.robogeddon.ui.widget.Widget;

public class SpawnPadSubMenu extends SelectableSubMenu {

    private final Stack stack = new Stack();

    public SpawnPadSubMenu(String id) {
        super(id);

        stack.add(new Label("Drone Type:").pad().bottom(10));

        for(DroneType type : EntityType.getDrones()){
            var button = new TextButton(type.getDisplayName());
            button.setOnClick((clickEvent, button1) -> selectType(type));
            button.setUserObject(type);
            button.setMinWidth(125);

            stack.add(button);
        }

        add(stack.pad().all(10));
    }

    private void selectType(DroneType type) {
        var message = new SpawnPadUpdateMessage(getTileEntity().getRootTile(), type);

        client.getWorld().sendMessageToServer(message);
    }

    @Override
    public void update() {
        var tileEntity = getTileEntity();

        if(!(tileEntity instanceof SpawnPadTileEntity))
            return;

        var spawnPad = (SpawnPadTileEntity) tileEntity;

        for(Widget widget : stack.getChildren()){
            if(widget instanceof Button){
                boolean selected = widget.getUserObject().equals(spawnPad.getCurrentType());

                ((Button) widget).setSelected(selected);
            }
        }
    }
}
