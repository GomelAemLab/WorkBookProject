package by.gomel.epam.core.validation;

import by.gomel.epam.core.beans.Event;


public class EventJsonValidation {
    private Event event;

    public EventJsonValidation(Event event) {
        this.event = event;
    }

    public void validate() throws ValidationError {
        //throw new ValidationError("");
    }
}
