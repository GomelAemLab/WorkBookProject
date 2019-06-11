package com.company.core.validation;

import com.company.core.execption.ValidationError;
import com.google.common.base.Strings;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

import static com.company.core.constants.Constants.*;

public class DateHelper {

    private String date;
    private String time;
    private boolean isTime;
    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;

    public DateHelper(String date) {
        this.date = date;
    }

    public DateHelper(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String validateAndGetPath() throws ValidationError {

        String path;
        try {
            if (Strings.isNullOrEmpty(date)) {
                throw new ValidationError(DATE_ERROR_MSG);
            }
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            if (!Strings.isNullOrEmpty(time)) {
                final LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ISO_TIME);
                hours = parsedTime.getHour();
                minutes = parsedTime.getMinute();
                isTime = true;
            }
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
        if (isTime) {
            calendar.set(Calendar.HOUR, hours);
            calendar.set(Calendar.MINUTE, minutes);
        }
        return calendar;
    }
}
