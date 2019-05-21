package by.gomel.epam.core.models;

import by.gomel.epam.core.beans.Event;
import by.gomel.epam.core.services.EventServiceCRUD;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Model(adaptables = Resource.class)
public class EventItemContainer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Self
    private Resource resource;

    @OSGiService
    private EventServiceCRUD eventServiceCRUD;

    @PostConstruct
    public void init() {
        logger.info("EventItemContainer model has been initialised.");
    }

    public List<Event> getEventItems() {
        if (eventServiceCRUD != null) {
            return eventServiceCRUD.getEvents();
        }
        logger.debug("EventServiceCRUD is null.");
        return null;
    }
}
