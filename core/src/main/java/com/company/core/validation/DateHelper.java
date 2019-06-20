package com.company.core.validation;

import com.company.core.execption.ValidationError;
import com.google.common.base.Strings;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

import static com.company.core.constants.Constants.DATE_ERROR_MSG;
import static com.company.core.constants.Constants.DATE_PATTERN;

public class DateHelper {

    private static final int CALENDAR_DIFF = 1;
    private String date;
    private String datePattern;
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

    public DateHelper(String date, String time) throws ValidationError {
        this.date = date;
        this.time = time;
        this.validate();
    }

    public DateHelper(String date, String time, String datePattern) throws ValidationError {
        this.date = date;
        this.time = time;
        this.datePattern = datePattern;
        this.validate();
    }
    public DateHelper(Calendar calendar) {
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + CALENDAR_DIFF;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.hours = calendar.get(Calendar.HOUR);
        this.minutes = calendar.get(Calendar.MINUTE);
    }

    void validate() throws ValidationError {

        try {
            if (Strings.isNullOrEmpty(date)) {
                throw new ValidationError(DATE_ERROR_MSG);
            }
            DateTimeFormatter formatter;
            if (Strings.isNullOrEmpty(this.datePattern)) {
                formatter = DateTimeFormatter.ISO_DATE;
            } else {
                formatter = DateTimeFormatter.ofPattern(this.datePattern);
            }
            LocalDate parsedDate = LocalDate.parse(date, formatter);
            if (!Strings.isNullOrEmpty(time)) {
                final LocalTime parsedTime = LocalTime.parse(time, DateTimeFormatter.ISO_TIME);
                hours = parsedTime.getHour();
                minutes = parsedTime.getMinute();
                isTime = true;
            }

            this.year = parsedDate.getYear();
            this.month = parsedDate.getMonthValue();
            this.day = parsedDate.getDayOfMonth();

        } catch (DateTimeParseException e) {
            throw new ValidationError(DATE_ERROR_MSG);
        }
    }

    public Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        int calendarMonthZeroBased = month - CALENDAR_DIFF;
        calendar.set(year, calendarMonthZeroBased, day);
        if (isTime) {
            calendar.set(Calendar.HOUR, hours);
            calendar.set(Calendar.MINUTE, minutes);
        }
        return calendar;
    }

    public String getDateHTML() {
        final DateTimeFormatter isoDate = DateTimeFormatter.ISO_DATE;
        return isoDate.format(LocalDate.of(year, month, day));
    }

    public String getTime() {
        final DateTimeFormatter isoDate = DateTimeFormatter.ISO_LOCAL_TIME;
        return isoDate.format(LocalTime.of(hours, minutes));
    }

    public String getDatePath() {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return formatter.format(LocalDate.of(year, month, day));
    }
}
