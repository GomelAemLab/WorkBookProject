package by.gomel.epam.core.validation;

import by.gomel.epam.core.beans.Event.Event;
import by.gomel.epam.core.execption.ValidationError;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static by.gomel.epam.core.constants.Constants.DATE_ERROR_MSG;
import static by.gomel.epam.core.constants.Constants.EVENT_PATH;
import static by.gomel.epam.core.constants.Constants.FOLDER_SEPARATOR;


public class EventJsonValidation {
    private Event event;

    public EventJsonValidation(Event event) {
        this.event = event;
    }

    public void validate() throws ValidationError {
        try {
            LocalDate parsedDate = LocalDate.parse(event.getEventDate());
            final StringBuilder sb = new StringBuilder(EVENT_PATH);
            sb.append(parsedDate.getYear());
            sb.append(FOLDER_SEPARATOR);
            sb.append(parsedDate.getMonthValue());
            sb.append(FOLDER_SEPARATOR);
            sb.append(parsedDate.getDayOfMonth());
            event.setEventFolderPath(sb.toString());
        } catch (DateTimeParseException e) {
            throw new ValidationError(DATE_ERROR_MSG);
        }
    }
}
