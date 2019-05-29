package com.company.core.validation;

import com.company.core.models.Event;
import com.company.core.execption.ValidationError;


public class EventJsonValidation {
    private Event event;

    public EventJsonValidation(Event event) {
        this.event = event;
    }

    public void validate() throws ValidationError {
            DateHelper dateValidation = new DateHelper(event.getEventDate());
            event.setEventFolderPath(dateValidation.validateAndGetPath());
    }
}
