package com.noahcharlton.robogeddon.ui.help;

import com.badlogic.gdx.utils.ObjectMap;
import com.noahcharlton.robogeddon.util.help.BlockHelpInfo;
import com.noahcharlton.robogeddon.util.help.HelpInfo;
import com.noahcharlton.robogeddon.util.help.HelpInfoLoader;

import java.util.Objects;

public class HelpPages {

    private static final ObjectMap<String, Class<? extends HelpScene>> pages = new ObjectMap<>();

    public static void init(){
        pages.put(HelpInfoLoader.BasicHelpInfo.guiID, BasicHelpPage.class);
        pages.put(BlockHelpInfo.guiID, BlockHelpPage.class);
    }

    public static HelpScene createFrom(HelpInfo info){
        var clazz = Objects.requireNonNull(pages.get(info.getGuiID()),
                "No Help Page registered with ID: " + info.getGuiID());

        try {
            return clazz.getConstructor(HelpInfo.class).newInstance(info);
        } catch(ReflectiveOperationException e) {
            throw new RuntimeException("Failed to create help page: " + info.getGuiID(), e);
        }
    }
}
