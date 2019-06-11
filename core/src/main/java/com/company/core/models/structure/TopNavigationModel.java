package com.company.core.models.structure;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

import static com.company.core.constants.Constants.HOME_ROOT_PAGE_PATH;

@Model(adaptables = {SlingHttpServletRequest.class})
public class TopNavigationModel {

    @Inject
    private SlingHttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    protected void init() {
        logger.info("Initialisation of model: {}", getClass());
    }

    /**
     * Returns path of the root page.
     *
     * @return
     */
    public String getHomeRootPagePath() {
        return HOME_ROOT_PAGE_PATH;
    }

    /**
     * Returns a list of pages that are used as the menu items.
     *
     * @return List of the pages.
     */
    public List<Page> getRootPages() {
        Resource rootResource = request.getResourceResolver().resolve(HOME_ROOT_PAGE_PATH);
        List<Page> pages = new LinkedList<>();
        for (Resource childPage : rootResource.getChildren()) {
            Page page = childPage.adaptTo(Page.class);
            if (page != null) {
                pages.add(page);
            }
        }
        return pages;
    }
}
