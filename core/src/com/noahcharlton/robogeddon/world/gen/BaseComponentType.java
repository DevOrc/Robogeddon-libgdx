package com.noahcharlton.robogeddon.world.gen;

import com.noahcharlton.robogeddon.Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BaseComponentType {

    POWER("solar", "mix", "solar_2"),
    ARTILLERY("turrets_1", "mix", "lasers_1", "turrets_2"),
    MISC("walls", "walls_2", "walls_3");

    private final String[] componentNames;
    private final List<BaseComponent> components = new ArrayList<>();

    BaseComponentType(String... componentNames) {
        this.componentNames = componentNames;
    }

    public static void preInit() {
        Arrays.stream(values()).forEach(BaseComponentType::createAsset);
    }

    private void createAsset() {
        Core.assets.registerAsset(new BaseComponentAsset(this));
    }

    public String getID() {
        return name().toLowerCase();
    }

    public List<BaseComponent> getComponents() {
        return components;
    }

    String[] getComponentNames() {
        return componentNames;
    }

}
