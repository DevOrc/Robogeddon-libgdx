package com.noahcharlton.robogeddon;

import java.util.ArrayList;
import java.util.stream.Stream;

public class OrderedRegistry<T extends HasID> extends Registry<T> {

    private final ArrayList<T> orderedValues = new ArrayList<>();

    @Override
    public void register(T item) {
        super.register(item);

        orderedValues.add(item);
    }

    public void registerAll(T... items){
        Stream.of(items).forEach(this::register);
    }

    public ArrayList<T> getOrderedValues() {
        return orderedValues;
    }
}
