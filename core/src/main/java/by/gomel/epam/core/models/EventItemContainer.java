package by.gomel.epam.core.models;

import by.gomel.epam.core.execption.JcrException;
import by.gomel.epam.core.services.EventServiceCRUD;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class)
public class EventItemContainer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @OSGiService
    private EventServiceCRUD eventServiceCRUD;

    private List<Event> events;

    @PostConstruct
    public void init() {
        try {
            if (eventServiceCRUD != null) {
                this.events = eventServiceCRUD.getEvents();
                logger.info("EventItemContainer model has been initialised.");
            } else {
                this.events = Collections.emptyList();
                logger.debug("EventServiceCRUD is null.");
            }
        } catch (JcrException e) {
            this.events = Collections.emptyList();
            logger.error("EventServiceCRUD service can't access to JCR by reason: {}", e.getMessage());
        }
    }

    public List<Event> getEventItems() {
        return events;
    }
}
