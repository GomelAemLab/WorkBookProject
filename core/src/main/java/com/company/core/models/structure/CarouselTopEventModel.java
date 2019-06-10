package com.company.core.models.structure;

import com.company.core.execption.JcrException;
import com.company.core.models.Event;
import com.company.core.services.EventServiceCRUD;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

@Model(adaptables = SlingHttpServletRequest.class)
public class CarouselTopEventModel {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int DEFAULT_NUMBER_OF_EVENTS = 874;

    @OSGiService
    private EventServiceCRUD eventServiceCRUD;

    private List<Event> closestEvents;

    @PostConstruct
    public void init() {
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        try {
            closestEvents = eventServiceCRUD.selectClosest(date, DEFAULT_NUMBER_OF_EVENTS);
        } catch (JcrException e) {
            logger.error("Cannot get closest events by reason: {}", e.getMessage());
            closestEvents = Collections.emptyList();
        }
    }

    public List<Event> getClosestEvents() {
        return closestEvents;
    }
}
