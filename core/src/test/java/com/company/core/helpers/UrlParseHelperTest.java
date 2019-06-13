package com.company.core.helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class UrlParseHelperTest {

    @Test
    public void getLocaleFromPathNotNullLocaleTest() {

        final String path = "http://localhost:4502/content/aem-workbook/en/events.html";

        String actual = UrlParseHelper.getLocaleFromPath(path);

        assertNotNull(actual);
    }

    @Test
    public void getLocaleFromPathRootPageLocaleTest() {

        final String path = "http://localhost:4502/content/aem-workbook/en.html";

        String expected = "/en";

        String actual = UrlParseHelper.getLocaleFromPath(path);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void getLocaleFromPathRuLocaleTest() {

        final String path = "http://localhost:4502/content/aem-workbook/ru/events.html";

        String expected = "/ru";
        String actual = UrlParseHelper.getLocaleFromPath(path);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void getLocaleFromPathEnLocaleTest() {

        final String path = "http://localhost:4502/content/aem-workbook/en/events.html";

        String expected = "/en";

        String actual = UrlParseHelper.getLocaleFromPath(path);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}