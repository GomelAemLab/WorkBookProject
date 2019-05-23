package by.gomel.epam.core.beans.Event;

import by.gomel.epam.core.beans.NodeProperty;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.lang.reflect.Field;

public class EventAdaptToNode {
    private Event event;

    public EventAdaptToNode(Event event) {
        this.event = event;
    }

    public void adaptTo(Node node) throws RepositoryException {

        Field[] fields = Event.class.getDeclaredFields();
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
