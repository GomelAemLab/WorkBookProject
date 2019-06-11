package com.company.core.validation;

import com.company.core.models.Event;
import com.company.core.execption.ValidationError;
import com.google.common.base.Strings;

import static com.company.core.constants.Constants.EMPTY_FIELD_ERROR_MSG;


public class EventValidation {
    private Event event;

    public EventValidation(Event event) {
        this.event = event;
    }

    public void validate() throws ValidationError {
        DateHelper dateValidation = new DateHelper(event.getEventDateHtmlFormat());
        event.setEventFolderPath(dateValidation.validateAndGetPath());
        event.setEventDate(dateValidation.getDate());
        validateNullOrEmpty(event.getEventName());
        validateNullOrEmpty(event.getEventUser());

    }

    private void validateNullOrEmpty(String value) throws ValidationError {
        if (Strings.isNullOrEmpty(value)) {
            throw new ValidationError(EMPTY_FIELD_ERROR_MSG);
        }
    }
}
