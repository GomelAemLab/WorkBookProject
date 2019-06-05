package com.company.core.constants;

public class Constants {
    public static final String EVENT_FOLDER_TYPE = "sling:OrderedFolder";
    public static final String EVENT_PATH = "/content/events/";
    public static final String EVENT_PATH_ARCHIVE = "/content/events/archive";
    public static final String DATE_ERROR_MSG = "a date cannot be processed";
    public static final String EMPTY_FIELD_ERROR_MSG = "empty field";
    public static final String FOLDER_SEPARATOR = "/";
    public static final String JOB_ARCHIVE = "archive";

    public static final int UNPROCESSABLE_ENTITY = 422;
    public static final int LOCKED = 423;
    public static final int NOT_FOUND = 404;
    public static final int BAD_REQUEST = 400;

    public static final String JOB_TOPIC_EVENT_CREATED = "com/sling/events/job";
    public static final String JOB_PROPERTY_EVENT_PATH = "event_path";
    public static final String FORM_SELECTOR = "form";
    public static final String REDIRECT_PATH = "redirectPath";
}
