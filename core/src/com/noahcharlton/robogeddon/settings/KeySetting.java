package com.noahcharlton.robogeddon.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

public class KeySetting extends Setting<Integer> {

    public KeySetting(String name, int value) {
        super(name, value);
    }

    @Override
    public void onButtonClick() {}

    @Override
    public String getButtonText() {
        return Input.Keys.toString(getValue());
    }

    @Override
    void save(XmlWriter writer) {
        writer.element(getTypeID(), getValue());
    }

    @Override
    void load(Element element) {
        setValue(element.getInt(getTypeID()));
    }

    public boolean isKeyJustPressed(){
        return Gdx.input.isKeyJustPressed(getValue());
    }

    public boolean isKeyPressed(){
        return Gdx.input.isKeyPressed(getValue());
    }
}
