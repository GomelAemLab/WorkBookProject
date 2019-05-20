package by.gomel.epam.core.services;

import by.gomel.epam.core.beans.Event;

import java.util.Date;
import java.util.List;

public interface EventServiceCRUD {

    String create(Event event);

    Event getEvent(String eventPath);

    List<Event> getEvents(String userName);

    List<Event> getEvents(Date date);

    List<Event> getEvents();

    Event update(String eventPath);

    boolean delete(String eventPath);
}
