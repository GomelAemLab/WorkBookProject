package com.company.core.models;

import com.company.core.services.EventServiceCRUD;
import com.company.core.validation.DateHelper;
import com.google.common.base.Strings;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.company.core.constants.Constants.EVENT_PATH;
import static com.company.core.constants.Constants.ID;

@Model(adaptables = SlingHttpServletRequest.class)
public class EventById {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @OSGiService
    private EventServiceCRUD eventServiceCRUD;

    @SlingObject
    private SlingHttpServletRequest request;

    public Event getEvent() {
        final String id = request.getParameter(ID);
        if (Strings.isNullOrEmpty(id)) {
            return null;
        }
        Event ev = null;
        try {
            ev = eventServiceCRUD.getEvent(EVENT_PATH + id);
            DateHelper dateHelper = new DateHelper(ev.getEventDate());
            ev.setEventDateHtmlFormat(dateHelper.getDateHTML());
            ev.setEventTime(dateHelper.getTime());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ev;
    }
}

