package com.company.core.models.structure;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

@Model(adaptables = {SlingHttpServletRequest.class})
public class TopNavigationModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String HOME_ROOT_PAGE_PATH = "/content/aem-workbook";
    private static final String PATH_DIVIDER = "/";
    private static final String HOME_ROOT_PAGE_LOCALE_DEFAULT = PATH_DIVIDER + "en";
    private static final String HOME_ROOT_PAGE_PATH_DEFAULT = HOME_ROOT_PAGE_PATH + HOME_ROOT_PAGE_LOCALE_DEFAULT;

    private static final int MAIN_CONTENT_STRING_PATH_INDEX = 1;
    private static final int LANGUAGE_SELECTOR_STRING_PATH_INDEX = 0;

    @Inject
    private SlingHttpServletRequest request;

    @Inject
    @Path(HOME_ROOT_PAGE_PATH)
    private Resource rootResource;

    @PostConstruct
    protected void init() {
        logger.info("Initialisation of model: {}", getClass());
    }

    public static String getHomeRootPagePath() {
        return HOME_ROOT_PAGE_PATH;
    }

    /**
     * Returns path of the root page.
     *
     * @return Current "Home" page path.
     */
    public String getCurrentHomeRootPagePath() {
        String locale = getLocaleFromPath(request.getRequestPathInfo().getResourcePath());
        return (locale != null) ?
                HOME_ROOT_PAGE_PATH + PATH_DIVIDER + locale.toLowerCase() :
                HOME_ROOT_PAGE_PATH + PATH_DIVIDER + HOME_ROOT_PAGE_LOCALE_DEFAULT;
    }

    private String getLocaleFromPath(final String path) {
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

    /**
     * Returns a list of pages that are used as the menu items.
     *
     * @return List of the pages.
     */
    public List<Page> getRootPages() {
        Resource resource = request.getResourceResolver().resolve(getCurrentHomeRootPagePath());
        List<Page> pages = new LinkedList<>();
        for (Resource childPage : resource.getChildren()) {
            Page page = childPage.adaptTo(Page.class);
            if (page != null) {
                pages.add(page);
            }
        }
        return pages;
    }

    /**
     * Returns list of the language pages.
     * @return List of the pages.
     */
    public List<Page> getLanguagePages() {
        List<Page> languages = new LinkedList<>();
        if (rootResource != null) {
            for (Resource childPage : rootResource.getChildren()) {
                Page page = childPage.adaptTo(Page.class);
                if (page != null) {
                    languages.add(page);
                }
            }
        }
        return languages;
    }

}
