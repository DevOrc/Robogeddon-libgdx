package com.noahcharlton.robogeddon.ui.selectable;

import com.noahcharlton.robogeddon.block.portal.UnloaderTileEntity;
import com.noahcharlton.robogeddon.ui.widget.Widget;

import java.util.HashMap;
import java.util.Objects;

public class SelectableSubMenus {

    private static final HashMap<String, Class<? extends SelectableSubMenu>> subMenus = new HashMap<>();

    public static void init(){
        add(UnloaderTileEntity.subMenuID, UnloaderSubMenu.class);
    }

    public static void add(String name, Class<? extends SelectableSubMenu> clazz){
        if(subMenus.containsKey(name))
            throw new UnsupportedOperationException("Duplicate ID: " + name);

        subMenus.put(name, clazz);
    }

    public static Widget createFrom(String name){
        var clazz = Objects.requireNonNull(subMenus.get(name), "No Sub Menu registered with ID: " + name);

        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch(ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create GUI: " + name, e);
        }
    }
}