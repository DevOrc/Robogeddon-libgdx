package com.noahcharlton.robogeddon.message;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class MessageSerializer {

    private static final Gson gson = new Gson();

    public static String messageToString(Message message){
        var data = Collections.singletonMap(message.getClass().getName(), message);

        return gson.toJson(data);
    }

    public static Message parse(String text) {
        JsonObject object = JsonParser.parseString(text).getAsJsonObject();
        Map.Entry<String, JsonElement> entry = object.entrySet().iterator().next();
        String clazz = entry.getKey();
        JsonElement message = entry.getValue();

        try {
            return gson.fromJson(message, (Type) Class.forName(clazz));
        } catch(ClassNotFoundException e) {
            throw new RuntimeException("Failed to find message type: " + clazz, e);
        }
    }
}
