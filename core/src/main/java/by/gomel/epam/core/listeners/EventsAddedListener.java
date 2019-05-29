package by.gomel.epam.core.listeners;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static by.gomel.epam.core.constants.Constants.EVENT_PATH;
import static by.gomel.epam.core.constants.Constants.JOB_PROPERTY_EVENT_PATH;
import static by.gomel.epam.core.constants.Constants.JOB_TOPIC_EVENT_CREATED;

@Component(service = ResourceChangeListener.class,
        immediate = true,
        property = {
                ResourceChangeListener.CHANGES + "=CHANGED",
                ResourceChangeListener.CHANGES + "=ADDED",
                ResourceChangeListener.PATHS + "=/content/events"
        })
public class EventsAddedListener implements ResourceChangeListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Pattern pathToEventsPattern;

    @Reference
    private JobManager jobManager;

    @Activate
    public void activate() {
        String pattern = EVENT_PATH + "(\\d+){3}/.+";
        pathToEventsPattern = Pattern.compile(pattern);
    }

    public void onChange(List<ResourceChange> changes) {

        changes.forEach(change -> {
            switch (change.getType()) {
                case ADDED: {
                    logger.debug("Change Type ADDED: {}", change);
                    final String changePath = change.getPath();
                    final Matcher m = pathToEventsPattern.matcher(changePath);
                    if (m.matches()) {
                        final Map<String, Object> props = new HashMap<>();
                        props.put(JOB_PROPERTY_EVENT_PATH, changePath);
                        jobManager.addJob(JOB_TOPIC_EVENT_CREATED, props);
                        logger.debug("Job ADDED: {}", JOB_TOPIC_EVENT_CREATED);
                    }
                }
                case CHANGED: {
                    //do something
                }
                default: {
                }
            }
        });
    }
}
