package com.noahcharlton.robogeddon.settings;

import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

import java.util.function.Consumer;

public class IntSetting extends Setting<Integer> {

    public IntSetting(String name, Integer value, Consumer<Integer> applier) {
        super(name, value, applier);
    }

    public IntSetting(String name, Integer value) {
        super(name, value);
    }

    @Override
    public void onButtonClick() {

    }

    @Override
    void save(XmlWriter writer) {
        writer.element(getTypeID(), getValue());
    }

    @Override
    void load(Element element) {
        setValue(element.getInt(getTypeID()));
    }
}
