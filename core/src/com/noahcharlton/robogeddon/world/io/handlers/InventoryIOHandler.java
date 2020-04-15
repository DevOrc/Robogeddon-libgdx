package com.noahcharlton.robogeddon.world.io.handlers;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.world.ServerWorld;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.WorldIOHandler;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

public class InventoryIOHandler implements WorldIOHandler {

    @Override
    public void save(XmlWriter xml, ServerWorld world) {
        for(var item : world.getInventory().getMap().entrySet()){
            xml.element(item.getKey(), item.getValue());
        }
    }

    @Override
    public void load(Element xml, ServerWorld world) {
        for(int i = 0; i < xml.getChildCount(); i++) {
            var child = xml.getChild(i);
            var item = Core.items.get(child.getName());

            world.getInventory().setItem(item, child.getTextAsInt());
        }
    }

    @Override
    public String getTypeID() {
        return "Inventory";
    }
}
