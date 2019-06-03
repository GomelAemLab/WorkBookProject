package com.company.core.beans.event;

import com.company.core.beans.NodeProperty;
import com.company.core.execption.ValidationError;
import com.company.core.models.Event;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.lang.reflect.Field;
import java.util.Map;

public class EventHelper {
    private static final Field[] fields = Event.class.getDeclaredFields();

    public Event fromMap(Map<String, String[]> keysValues) throws ValidationError {

        final Event event = new Event();
        for (Field field : fields) {

            field.setAccessible(true);
            if (field.isAnnotationPresent(NodeProperty.class)) {
                final String type = field.getType().getTypeName();
                final String fieldName = field.getName();
                String value;
                if (type.equals("java.lang.String") && (value = keysValues.get(fieldName)[0]) != null) {
                    try {
                        field.set(event, value);
                    } catch (IllegalAccessException e) {
                        throw new ValidationError();
                    }
                }
            }
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
                    }
                }
            } catch (Exception e) {
                throw new RepositoryException();
            }
        }
    }
}
