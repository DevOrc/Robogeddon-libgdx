package com.noahcharlton.robogeddon.message;

import com.google.gson.*;
import com.noahcharlton.robogeddon.HasID;
import com.noahcharlton.robogeddon.Registry;

import java.lang.reflect.Type;

public class RegistrySerializer<T extends HasID> implements JsonSerializer<T>, JsonDeserializer<T> {

    private final Registry<T> registry;

    public RegistrySerializer(Registry<T> registry) {
        this.registry = registry;
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTypeID());
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return registry.get(json.getAsString());
    }
}
