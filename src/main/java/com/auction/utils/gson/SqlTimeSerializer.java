package com.auction.utils.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class SqlTimeSerializer implements JsonSerializer<Time> {

    private static final String PATTERN = "HH:mm:ss";
    private static final DateFormat formatter = new SimpleDateFormat(PATTERN);

    @Override
    public JsonElement serialize(Time date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(date));
    }
}
