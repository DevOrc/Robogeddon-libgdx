package com.noahcharlton.robogeddon.world.fluid;

import com.badlogic.gdx.graphics.Color;
import com.noahcharlton.robogeddon.HasID;

import java.util.Objects;

public class Fluid implements HasID {

    private final String typeID;
    private final String displayName;
    private final Color color;

    public Fluid(String typeID, String name, Color color) {
        this.typeID = typeID;
        this.displayName = name;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Color getColor() {
        return color;
    }

    public Color getColor(float transparency) {
        var color = this.color.cpy();
        color.a = transparency;

        return color;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Fluid)) return false;
        Fluid fluid = (Fluid) o;
        return getTypeID().equals(fluid.getTypeID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTypeID());
    }

    @Override
    public String getTypeID() {
        return typeID;
    }
}
