package com.auction.utils.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class SqlDateSerializer implements JsonSerializer<Date> {

    private static final String PATTERN = "yyyy-MM-dd";
    private static final DateFormat formatter = new SimpleDateFormat(PATTERN);

    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(date));
    }
}
