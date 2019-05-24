package by.gomel.epam.core.validation;

import by.gomel.epam.core.models.Event;
import by.gomel.epam.core.execption.ValidationError;


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
