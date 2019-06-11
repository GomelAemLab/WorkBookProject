package com.company.core.validation;

import com.company.core.execption.ValidationError;
import com.google.common.base.Strings;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

import static com.company.core.constants.Constants.DATE_ERROR_MSG;
import static com.company.core.constants.Constants.EVENT_PATH;
import static com.company.core.constants.Constants.FOLDER_SEPARATOR;

public class DateHelper {

    private String date;
    private int year;
    private int month;
    private int day;

    public DateHelper(String date) {
        this.date = date;
    }

    public String validateAndGetPath() throws ValidationError {

        String path;
        try {
            if (Strings.isNullOrEmpty(date)) {
                throw new ValidationError(DATE_ERROR_MSG);
            }
            LocalDate parsedDate = LocalDate.parse(date);
            final StringBuilder sb = new StringBuilder(EVENT_PATH);
            this.year = parsedDate.getYear();
            sb.append(year);
            sb.append(FOLDER_SEPARATOR);
            this.month = parsedDate.getMonthValue();
            sb.append(month);
            sb.append(FOLDER_SEPARATOR);
            this.day = parsedDate.getDayOfMonth();
            sb.append(day);
            path = sb.toString();
        } catch (DateTimeParseException e) {
            throw new ValidationError(DATE_ERROR_MSG);
        }
        return path;
    }

    public Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        int calendarMonthZeroBased = month - 1;
        calendar.set(year, calendarMonthZeroBased, day);
        return calendar;
    }
}
