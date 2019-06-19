package com.company.core.services;

import com.company.core.execption.HttpException;
import com.company.core.execption.NotFoundException;
import com.company.core.models.Event;
import com.company.core.execption.JcrException;
import com.company.core.execption.ValidationError;
import org.apache.sling.api.resource.LoginException;

import java.util.Calendar;
import java.util.List;

public interface EventServiceCRUD {

    String create(String path, Event event) throws JcrException;

    Event getEvent(String eventPath) throws LoginException, NotFoundException, JcrException;

    List<Event> getEvents(String userName);

    List<Event> getEventsFromDate(String date) throws ValidationError, HttpException;

    List<Event> getEvents() throws JcrException;

    void update(Event event) throws JcrException, NotFoundException, ValidationError;

    void delete(String eventPath) throws NotFoundException, JcrException;

    List<Event> selectClosest(Calendar fromDate, int limit) throws JcrException;
}
