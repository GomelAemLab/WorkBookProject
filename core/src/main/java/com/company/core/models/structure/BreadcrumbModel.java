package com.company.core.models.structure;

import com.company.core.beans.breadcrumb.BreadcrumbHelper;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class)
public class BreadcrumbModel {

    @Self
    private SlingHttpServletRequest request;

    private List<Page> pageChain;

    @PostConstruct
    public void init() {
        String path = request.getRequestPathInfo().getResourcePath();
        pageChain = BreadcrumbHelper.getPagesInPath(path, request.getResourceResolver());
    }

    public List<Page> getPageChain() {
        return pageChain;
    }
}
