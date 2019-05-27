package by.gomel.epam.core.validation;

import by.gomel.epam.core.execption.ValidationError;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static by.gomel.epam.core.constants.Constants.DATE_ERROR_MSG;
import static by.gomel.epam.core.constants.Constants.EVENT_PATH;
import static by.gomel.epam.core.constants.Constants.FOLDER_SEPARATOR;

public class DateHelper {

    private String date;

    public DateHelper(String date) {
        this.date = date;
    }

    public String validateAndGetPath() throws ValidationError {

        String path;
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            final StringBuilder sb = new StringBuilder(EVENT_PATH);
            sb.append(parsedDate.getYear());
            sb.append(FOLDER_SEPARATOR);
            sb.append(parsedDate.getMonthValue());
            sb.append(FOLDER_SEPARATOR);
            sb.append(parsedDate.getDayOfMonth());
            path = sb.toString();
        } catch (DateTimeParseException e) {
            throw new ValidationError(DATE_ERROR_MSG);
        }
        return path;
    }
}
