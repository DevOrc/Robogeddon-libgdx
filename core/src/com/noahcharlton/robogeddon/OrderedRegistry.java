package com.noahcharlton.robogeddon;

import java.util.ArrayList;

public class OrderedRegistry<T extends HasID> extends Registry<T> {

    private final ArrayList<T> orderedValues = new ArrayList<>();

    @Override
    public void register(T item) {
        super.register(item);

        orderedValues.add(item);
    }

    public ArrayList<T> getOrderedValues() {
        return orderedValues;
    }
}
