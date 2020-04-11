package com.noahcharlton.robogeddon.message;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class MessageSerializer {

    private static final GsonBuilder builder = new GsonBuilder();
    private static Gson gson;

    public static void registerType(Type type, Object adapter){
        if(gson != null)
            throw new UnsupportedOperationException("Cannot register type after serializer has been finalized!");

        builder.registerTypeAdapter(type, adapter);
    }

    public static void finalizeSerializer(){
        if(gson != null)
            throw new UnsupportedOperationException("Serializer has already been finalized!");

        gson = builder.create();
    }

    public static String messageToString(Message message){
        if(gson == null)
            throw new UnsupportedOperationException("Cannot serialize messages before serializer has been finalized");

        var data = Collections.singletonMap(message.getClass().getName(), message);

        return gson.toJson(data);
    }

    public static Message parse(String text) {
        if(gson == null)
            throw new UnsupportedOperationException("Cannot parse messages before serializer has been finalized");

        JsonObject object = JsonParser.parseString(text).getAsJsonObject();
        Map.Entry<String, JsonElement> entry = object.entrySet().iterator().next();
        String clazz = entry.getKey();
        JsonElement message = entry.getValue();

        try {
            return gson.fromJson(message, (Type) Class.forName(clazz));
        } catch(ClassNotFoundException e) {
            throw new RuntimeException("Failed to find message type: " + clazz, e);
        } catch(RuntimeException e){
            throw new RuntimeException("Failed to parse message: " + text, e);
        }
    }
}
