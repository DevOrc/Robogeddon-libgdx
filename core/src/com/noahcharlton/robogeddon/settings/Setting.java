package com.noahcharlton.robogeddon.settings;

import com.noahcharlton.robogeddon.Core;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.Log;
import com.noahcharlton.robogeddon.world.io.Element;
import com.noahcharlton.robogeddon.world.io.XmlWriter;

import java.util.function.Consumer;

public abstract class Setting<T> implements HasID {

    private final String name;
    private final Consumer<T> applier;
    private T value;

    public Setting(String name, T value, Consumer<T> applier) {
        this.name = name;
        this.value = value;
        this.applier = applier;
    }

    public Setting(String name, T value) {
        this(name, value, x -> {});
    }

    public static void applyAll(){
        Log.info("Applying settings!");
        Core.settings.values().forEach(Setting::apply);
    }

    private void apply() {
        applier.accept(value);
    }

    String getButtonText(){
        return value.toString();
    }

    abstract void onButtonClick();

    abstract void save(XmlWriter writer);

    abstract void load(Element element);

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String getTypeID() {
        return name;
    }
}
