package com.company.core.validation;

import com.company.core.models.Event;
import com.company.core.execption.ValidationError;
import com.google.common.base.Strings;

import java.util.Calendar;

import static com.company.core.constants.Constants.EMPTY_FIELD_ERROR_MSG;


public class EventValidation {
    private Event event;

    public EventValidation(Event event) {
        this.event = event;
    }

    public void validate(boolean checkDate) throws ValidationError {
        if (checkDate) {
            validateNullOrEmpty(event.getEventDate());
        } else {
            validateNullOrEmpty(event.getEventTime());
        }
        validateNullOrEmpty(event.getEventName());
        validateNullOrEmpty(event.getEventUser());

    }

    private void validateNullOrEmpty(Calendar value) throws ValidationError {
        if (value == null) {
            throw new ValidationError(EMPTY_FIELD_ERROR_MSG);
        }
    }
    private void validateNullOrEmpty(String value) throws ValidationError {
        if (Strings.isNullOrEmpty(value)) {
            throw new ValidationError(EMPTY_FIELD_ERROR_MSG);
        }
    }
}
