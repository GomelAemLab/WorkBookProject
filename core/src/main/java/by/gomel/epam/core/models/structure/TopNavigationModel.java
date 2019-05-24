package by.gomel.epam.core.models.structure;

import by.gomel.epam.core.utils.PageNavigationUtil;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Model(adaptables = {SlingHttpServletRequest.class})
public class TopNavigationModel {

    @Inject
    private SlingHttpServletRequest request;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String SECTION_PATH_REGEX = "(?=/)(/([a-z0-9-]*))(?=/)";

    private static final int FIRST_SITE_LEVEL_INDEX = 0;
    private static final int SECOND_SITE_LEVEL_INDEX = 1;

    @PostConstruct
    protected void init() {
        logger.info("Initialisation of model: {}", getClass());
    }

    /**
     * Returns a list of pages that are used as the menu items.
     *
     * @return List of the pages.
     */
    public List<Page> getRootPages() {

        Pattern pattern = Pattern.compile(SECTION_PATH_REGEX);
        Matcher matcher = pattern.matcher(request.getRequestPathInfo().getResourcePath());
        List<String> pathList = new ArrayList<>();
        while (matcher.find()) {
            pathList.add(matcher.group());
        }
        String rootPath = pathList.get(FIRST_SITE_LEVEL_INDEX) + pathList.get(SECOND_SITE_LEVEL_INDEX);
        return PageNavigationUtil.getPagesByPath(request.getResourceResolver(), rootPath);
    }
}
