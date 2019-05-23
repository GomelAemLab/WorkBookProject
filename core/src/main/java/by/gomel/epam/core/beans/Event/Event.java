package by.gomel.epam.core.beans.Event;

import by.gomel.epam.core.beans.NodeProperty;
import com.google.gson.annotations.SerializedName;

public class Event {

    @NodeProperty
    @SerializedName(value = "eventText")
    private String eventName;

    private String eventFolderPath;
    private String eventDate;
    @NodeProperty
    private String eventTime;
    @NodeProperty
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

