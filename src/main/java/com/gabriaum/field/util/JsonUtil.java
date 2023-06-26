package com.gabriaum.field.util;

import com.gabriaum.field.Core;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mongodb.BasicDBObject;
import org.bson.BsonInvalidOperationException;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

    public static JsonObject jsonTree(Object src) {
        return Core.GSON.toJsonTree(src).getAsJsonObject();
    }

    public static Object elementToBson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            return primitive.isString() ? primitive.getAsString() : primitive.isNumber() ? primitive.getAsNumber() : primitive.getAsBoolean();
        }

        try {
            return Document.parse(Core.GSON.toJson(element));
        } catch (BsonInvalidOperationException ex) {
            return BasicDBObject.parse(Core.GSON.toJson(element));
        }
    }

    public static String elementToString(JsonElement element) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                return primitive.getAsString();
            }
        }
        return Core.GSON.toJson(element);
    }

    public static <T> T mapToObject(Map<String, String> map, Class<T> tClass) {
        JsonObject object = new JsonObject();

        map.forEach((key, value) -> {
            try {
                object.add(key, Core.JSON.parse(value));
            } catch (Exception e) {
                object.addProperty(key, value);
            }
        });

        return Core.GSON.fromJson(object, tClass);
    }

    public static Map<String, String> objectToMap(Object src) {
        Map<String, String> map = new HashMap<>();
        try {
            JsonObject object = (JsonObject) Core.GSON.toJsonTree(src);
            object.entrySet().forEach(entry -> map.put(entry.getKey(), Core.GSON.toJson(entry.getValue())));
        } catch (Exception ignored) {
        }

        return map;
    }
}