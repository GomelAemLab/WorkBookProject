package by.gomel.epam.core.models;

import by.gomel.epam.core.execption.JcrException;
import by.gomel.epam.core.services.EventServiceCRUD;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class)
public class EventItemContainer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @OSGiService
    private EventServiceCRUD eventServiceCRUD;

    @PostConstruct
    public void init() {
        logger.info("EventItemContainer model has been initialised.");
    }

    public List<Event> getEventItems() {
        if (eventServiceCRUD != null) {
            try {
                return eventServiceCRUD.getEvents();
            } catch (JcrException e) {
                logger.error("service can't access to JCR");
            }
        }
        logger.debug("EventServiceCRUD is null.");
        return null;
    }
}
