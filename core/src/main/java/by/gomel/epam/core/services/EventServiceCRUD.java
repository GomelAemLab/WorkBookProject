package by.gomel.epam.core.services;

import by.gomel.epam.core.execption.HttpException;
import by.gomel.epam.core.models.Event;
import by.gomel.epam.core.execption.JcrException;
import by.gomel.epam.core.execption.ValidationError;

import java.util.List;

public interface EventServiceCRUD {

    String create(Event event) throws JcrException;

    Event getEvent(String eventPath);

    List<Event> getEvents(String userName);

    List<Event> getEventsFromDate(String date) throws ValidationError, HttpException;

    List<Event> getEvents() throws JcrException;

    Event update(String eventPath);

    boolean delete(String eventPath);
}
