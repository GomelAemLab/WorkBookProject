package com.company.core.models;

import com.company.core.beans.NodeProperty;
import com.google.gson.annotations.SerializedName;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables=Resource.class)
public class Event {

    @NodeProperty
    @SerializedName(value = "eventText")
    @Inject
    private String eventName;

    private String eventFolderPath;

    @NodeProperty
    @Inject
    private String eventDate;
    @NodeProperty
    @Inject
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

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
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
}

