package com.noahcharlton.robogeddon;

import java.util.Collection;
import java.util.HashMap;

public class Registry<T extends HasID>  {

    private final HashMap<String, T> items = new HashMap<>();

    private boolean finalized;

    public void register(T item){
        if(finalized){
            throw new UnsupportedOperationException();
        }else if(items.containsKey(item.getTypeID())){
            throw new IllegalArgumentException("Duplicate id: " + item.getTypeID());
        }else if(item.getTypeID() == null || item.getTypeID().isEmpty()){
            throw new IllegalArgumentException();
        }

        items.put(item.getTypeID(), item);
        Log.trace("Registered " + item.getTypeID() + ": " + item.getClass().getName());
    }

    public T get(String id){
        var item = items.get(id);

        if(item == null)
            throw new RuntimeException("No item found with id " + id);

        return item;
    }

    public T getOrNull(String id){
        return items.get(id);
    }

    void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    public Collection<T> values(){
        return items.values();
    }
}
