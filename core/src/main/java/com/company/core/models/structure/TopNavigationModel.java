package com.company.core.models.structure;

import com.company.core.helpers.UrlParseHelper;
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

import static com.company.core.constants.Constants.*;

@Model(adaptables = {SlingHttpServletRequest.class})
public class TopNavigationModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private SlingHttpServletRequest request;

    @Inject
    @Path(HOME_ROOT_PAGE_PATH)
    private Resource rootResource;

    private List<Page> languagePages = new LinkedList<>();
    private List<Page> rootPages = new LinkedList<>();
    private String currentHomeRootPagePath;

    @PostConstruct
    protected void init() {
        logger.info("Initialisation of model: {}", getClass());

        if (rootResource != null) {
            for (Resource childPage : rootResource.getChildren()) {
                Page page = childPage.adaptTo(Page.class);
                if (page != null) {
                    languagePages.add(page);
                }
            }
        }

        String locale = UrlParseHelper.getLocaleFromPath(request.getRequestPathInfo().getResourcePath());
        currentHomeRootPagePath = (locale != null) ?
                HOME_ROOT_PAGE_PATH + locale.toLowerCase() :
                HOME_ROOT_PAGE_PATH + HOME_ROOT_PAGE_LOCALE_DEFAULT;

        Resource resource = request.getResourceResolver().resolve(getCurrentHomeRootPagePath());

        for (Resource childPage : resource.getChildren()) {
            Page page = childPage.adaptTo(Page.class);
            if (page != null) {
                rootPages.add(page);
            }
        }
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
        return currentHomeRootPagePath;
    }

    /**
     * Returns a list of pages that are used as the menu items.
     *
     * @return List of the pages.
     */
    public List<Page> getRootPages() {
        return rootPages;
    }

    /**
     * Returns list of the language pages.
     *
     * @return List of the pages.
     */
    public List<Page> getLanguagePages() {
        return languagePages;
    }

}
