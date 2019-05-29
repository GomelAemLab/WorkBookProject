package by.gomel.epam.core.listeners;

import by.gomel.epam.core.configuration.UserPrincipal;
import com.google.common.base.Strings;
import org.apache.sling.api.resource.*;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static by.gomel.epam.core.constants.Constants.JOB_PROPERTY_EVENT_PATH;
import static by.gomel.epam.core.constants.Constants.JOB_TOPIC_EVENT_CREATED;

@Component(service = JobConsumer.class,
        immediate = true,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + JOB_TOPIC_EVENT_CREATED,
        })

public class ProcessingEventCreation implements JobConsumer {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<String, Object> param = new HashMap<>();

    @Reference
    private ResourceResolverFactory resolverFactory;
    @Reference
    private UserPrincipal userPrincipal;

    @Activate
    @Modified
    protected final void activate() {
        param.put(ResourceResolverFactory.SUBSERVICE, userPrincipal.getUserMappingPrincipal());
    }

    @Override
    public JobResult process(Job job) {
        final String eventPath = (String) job.getProperty(JOB_PROPERTY_EVENT_PATH);
        if (Strings.isNullOrEmpty(eventPath)) {
            logger.debug("the job with an empty payload");
            return JobResult.CANCEL;
        }
        JobResult result = JobResult.FAILED;
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {

            final Resource event = resolver.getResource(eventPath);
            if (event != null) {
                final ModifiableValueMap map = event.adaptTo(ModifiableValueMap.class);
                if (map != null) {
                    map.put("eventCreated", Calendar.getInstance());
                    resolver.commit();
                    result=JobResult.OK;
                }
            }
        } catch (LoginException | PersistenceException e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }
}
