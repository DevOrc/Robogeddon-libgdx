package com.noahcharlton.robogeddon.settings;

import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

import java.util.function.Consumer;

public class BooleanSetting extends Setting<Boolean>{

    public BooleanSetting(String name, boolean value, Consumer<Boolean> applier) {
        super(name, value, applier);
    }

    public BooleanSetting(String name, boolean value) {
        super(name, value);
    }

    @Override
    public void onButtonClick() {
        setValue(!getValue());
    }

    @Override
    public String getButtonText() {
        return getValue() ? "On" : "Off";
    }

    @Override
    void save(XmlWriter writer) {
        writer.element(getTypeID(), getValue());
    }

    @Override
    void load(Element element) {
        String text = element.getChildByName(getTypeID()).getText();

        setValue(Boolean.valueOf(text));
    }
}