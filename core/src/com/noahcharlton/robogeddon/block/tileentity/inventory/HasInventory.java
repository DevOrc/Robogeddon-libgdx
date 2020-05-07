package com.noahcharlton.robogeddon.block.tileentity.inventory;

import com.noahcharlton.robogeddon.message.MessageSerializer;
import com.noahcharlton.robogeddon.util.Direction;
import com.noahcharlton.robogeddon.util.Side;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlWriter;
import com.noahcharlton.robogeddon.world.item.Item;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface HasInventory {

    @Side(Side.SERVER)
    boolean acceptItem(Item item);

    @Side(Side.CLIENT)
    void setBuffers(ItemBuffer[] buffers);

    @Side(Side.SERVER)
    Item retrieveItem(boolean simulate, Direction to);

    ItemBuffer[] getBuffers();

    /**
     * Used by transport tile entities
     * to determine if the can be connected
     */
    @Side(Side.SERVER)
    default boolean canConnect(Direction from, Direction beltDirection){
        return true;
    }

    default String[] getInventoryDetails() {
        var buffers = Stream.of(getBuffers()).filter(b -> b.currentItem() != null).collect(Collectors.toList());
        String[] data = new String[buffers.size() + 1];
        data[0] = " \nInventory:";

        for(int i = 0; i < buffers.size(); i++) {
            var buffer = buffers.get(i);
            data[i + 1] = buffer.currentItem().getDisplayName() + ": " + buffer.getAmount();
        }

        return data;
    }

    static void save(HasInventory inventory, XmlWriter xml) {
        var element = xml.element("Inventory");

        for(ItemBuffer buffer : inventory.getBuffers()){
            element.element("Buffer", MessageSerializer.toJson(buffer, ItemBuffer.class));
        }

        element.pop();
    }

    static void load(HasInventory inventory, Element xml) {
        var element = xml.getChildByName("Inventory");
        var buffers = new ItemBuffer[element.getChildCount()];

        for(int i = 0; i < buffers.length; i++) {
            buffers[i] = MessageSerializer.fromJson(element.getChild(i).getText(), ItemBuffer.class);
        }

        inventory.setBuffers(buffers);
    }

}
