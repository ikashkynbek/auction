package com.auction.utils.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class DateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final SimpleDateFormat formatter = new SimpleDateFormat(PATTERN);


    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(formatter.format(date));
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            return  formatter.parse(json.getAsString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
