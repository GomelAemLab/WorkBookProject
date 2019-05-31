package com.company.core.helpers;

import static com.company.core.constants.Constants.*;

public class UrlParseHelper {

    private static final int MAIN_CONTENT_STRING_PATH_INDEX = 1;
    private static final int LANGUAGE_SELECTOR_STRING_PATH_INDEX = 0;

    public static String getLocaleFromPath(final String path) {
        String[] paths = path.split(HOME_ROOT_PAGE_PATH + PATH_DIVIDER);
        if (paths.length < 2) {
            return HOME_ROOT_PAGE_PATH_DEFAULT;
        }
        String mainPathPart = paths[MAIN_CONTENT_STRING_PATH_INDEX];

        String[] secondPathParts = mainPathPart.split(PATH_DIVIDER);
        if (secondPathParts.length < 2) {
            return HOME_ROOT_PAGE_PATH_DEFAULT;
        }
        return secondPathParts[LANGUAGE_SELECTOR_STRING_PATH_INDEX];
    }
}
