package com.auction.utils.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class LocalTimeSerializer implements JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {

    private static final String PATTERN = "HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);

    @Override
    public JsonElement serialize(LocalTime time, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(time.format(formatter));
    }

    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return LocalTime.parse(json.getAsString(), formatter);
    }
}
