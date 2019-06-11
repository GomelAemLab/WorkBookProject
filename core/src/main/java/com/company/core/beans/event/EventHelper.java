package com.company.core.beans.event;

import com.company.core.beans.NodeProperty;
import com.company.core.execption.ValidationError;
import com.company.core.models.Event;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Map;
import java.util.stream.Collectors;

public class EventHelper {
    private static final Field[] fields = Event.class.getDeclaredFields();

    public Event fromMap(Map<String, String[]> keysValues) throws ValidationError {
        final Gson gson = new Gson();
        Event event;
        final String mapValues = gson.toJson(keysValues.entrySet()
                .stream().collect(
                        Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0])));
        try {
            event = gson.fromJson(mapValues, Event.class);
        } catch (JsonSyntaxException e) {
            throw new ValidationError(e.getMessage());
        }
        return event;
    }

    public void setPropertiesToNode(Event event, Node node) throws RepositoryException {

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.isAnnotationPresent(NodeProperty.class)) {
                    final String type = field.getType().getTypeName();
                    if (type.equals("java.lang.String")) {
                        final String fieldName = field.getName();
                        final Object fieldValue = field.get(event);
                        final String value = String.class.cast(fieldValue);
                        node.setProperty(fieldName, value);
                    } else if (type.equals("java.util.Calendar")) {
                        final String fieldName = field.getName();
                        final Object fieldValue = field.get(event);
                        final Calendar value = Calendar.class.cast(fieldValue);
                        node.setProperty(fieldName, value);
                    }
                }
            } catch (Exception e) {
                throw new RepositoryException();
            }
        }
    }
}

