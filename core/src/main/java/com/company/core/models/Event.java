package com.company.core.models;

import com.company.core.beans.NodeProperty;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.Calendar;

@Model(adaptables=Resource.class)
public class Event {

    private String id;

    @NodeProperty
    @Inject
    private Calendar eventDate;

    @NodeProperty
    @Inject
    private String eventName;

    private String eventDateHtmlFormat;

    private String eventTime;

    @NodeProperty
    @Inject
    private String eventUser;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDateHtmlFormat() {
        return eventDateHtmlFormat;
    }

    public void setEventDateHtmlFormat(String eventDate) {
        this.eventDateHtmlFormat = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventUser() {
        return eventUser;
    }

    public void setEventUser(String eventUser) {
        this.eventUser = eventUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calendar getEventDate() {
        return eventDate;
    }

    public void setEventDate(Calendar eventDate) {
        this.eventDate = eventDate;
    }
}

