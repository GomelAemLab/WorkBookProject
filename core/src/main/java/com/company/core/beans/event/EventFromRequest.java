package com.company.core.beans.event;

import com.company.core.execption.ValidationError;
import com.company.core.models.Event;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Map;

public class EventFromRequest {

    public Event get(Map<String, String[]> parameterMap) throws ValidationError {
        EventHelper helper = new EventHelper();
        return helper.fromMap(parameterMap);
    }

    public Event get(final String json) throws ValidationError {
        final Gson gson = new Gson();
        Event event;
        try {
            event = gson.fromJson(json, Event.class);
        } catch (JsonSyntaxException e) {
            throw new ValidationError();
        }
        return event;
    }
}
