package com.company.core.beans.breadcrumb;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.company.core.constants.Constants.HOME_ROOT_PAGE_PATH;

public class BreadcrumbHelper {

    public static List<Page> getPagesInPath(String path, ResourceResolver resourceResolver) {

        List<Page> chainPages = new ArrayList<>();
        Resource resource = resourceResolver.getResource(path);

        while (resource != null) {
            Page page = resource.adaptTo(Page.class);
            if (HOME_ROOT_PAGE_PATH.equals(resource.getPath())) {
                break;
            }
            if (page != null) {
                chainPages.add(page);
            }
            resource = resource.getParent();

        }
        Collections.reverse(chainPages);
        return chainPages;
    }
}
