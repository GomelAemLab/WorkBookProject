package by.gomel.epam.core.utils;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.LinkedList;
import java.util.List;

public class PageNavigationUtil {

    /**
     * Gets a list of pages by path.
     *
     * @param resourceResolver ResourceResolver object.
     * @param path             The Path to the root folder of pages.
     * @return The List of pages.
     */
    public static List<Page> getPagesByPath(ResourceResolver resourceResolver, String path) {

        Resource rootResource = resourceResolver.resolve(path);
        List<Page> pages = new LinkedList<>();
        for (Resource childPage : rootResource.getChildren()) {
            Page page = childPage.adaptTo(Page.class);
            pages.add(page);
        }
        return pages;
    }
}
