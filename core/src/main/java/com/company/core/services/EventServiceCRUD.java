package com.company.core.services;

import com.company.core.execption.HttpException;
import com.company.core.models.Event;
import com.company.core.execption.JcrException;
import com.company.core.execption.ValidationError;

import java.util.Calendar;
import java.util.List;

public interface EventServiceCRUD {

    String create(Event event) throws JcrException;

    Event getEvent(String eventPath);

    List<Event> getEvents(String userName);

    List<Event> getEventsFromDate(String date) throws ValidationError, HttpException;

    List<Event> getEvents() throws JcrException;

    Event update(String eventPath);

    boolean delete(String eventPath);

    List<Event> selectClosest(Calendar fromDate, int limit) throws JcrException;
}
