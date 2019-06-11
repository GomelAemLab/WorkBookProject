package com.company.core.models;

import com.company.core.beans.NodeProperty;
import com.google.gson.annotations.SerializedName;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

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

    private String eventFolderPath;

    private String eventDateHtmlFormat;
    @NodeProperty
    @Inject
    @Optional
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

    public String getEventFolderPath() {
        return eventFolderPath;
    }
    public void setEventFolderPath(String eventFolderPath) {
        this.eventFolderPath = eventFolderPath;
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

