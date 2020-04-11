package com.noahcharlton.robogeddon.message;

import com.google.gson.*;

import java.lang.reflect.Type;

public final class InterfaceSerializer<T>
        implements JsonSerializer<T>, JsonDeserializer<T> {

    public static <T> InterfaceSerializer<T> create() {
        return new InterfaceSerializer<>();
    }

    @Override
    public JsonElement serialize(final T value, final Type type, final JsonSerializationContext context) {
        final Type targetType = value != null 
                ? value.getClass()
                : type;
        String clazzName = value == null ? "null" : value.getClass().getName();
        JsonObject element = new JsonObject();
        element.add("class", new JsonPrimitive(clazzName));
        element.add("data", context.serialize(value, targetType));
        return element;
    }

    @Override
    public T deserialize(final JsonElement jsonElement, final Type typeOfT, final JsonDeserializationContext context) {
        var obj = jsonElement.getAsJsonObject();
        var clazz = obj.get("class").getAsString();
        var data = obj.get("data");

        if(clazz.equals("null"))
            return null;

        try {
            return context.deserialize(data, Class.forName(clazz));
        } catch(ClassNotFoundException e) {
            throw new RuntimeException("Failed to find class: " + clazz, e);
        }
    }

}