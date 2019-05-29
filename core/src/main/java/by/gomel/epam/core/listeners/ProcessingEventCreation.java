package by.gomel.epam.core.listeners;

import by.gomel.epam.core.configuration.Configuration;
import com.google.common.base.Strings;
import org.apache.jackrabbit.value.DateValue;
import org.apache.sling.api.resource.*;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static by.gomel.epam.core.constants.Constants.JOB_PROPERTY_EVENT_PATH;
import static by.gomel.epam.core.constants.Constants.JOB_TOPIC_EVENT_CREATED;

@Component(service = JobConsumer.class,
        immediate = true,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=" + JOB_TOPIC_EVENT_CREATED,
        })
@Designate(
        ocd = Configuration.class
)
public class ProcessingEventCreation implements JobConsumer {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Map<String, Object> param = new HashMap<>();

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Activate
    @Modified
    protected final void activate(Configuration config) {
        param.put(ResourceResolverFactory.SUBSERVICE, config.user_mapping_principal());
    }

    @Override
    public JobResult process(Job job) {
        final String eventPath = (String) job.getProperty(JOB_PROPERTY_EVENT_PATH);
        if (Strings.isNullOrEmpty(eventPath)) {
            logger.debug("the job with an empty payload");
            return JobResult.CANCEL;
        }

        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {

            final Resource event = resolver.getResource(eventPath);
            if (event != null) {
                final Node node = event.adaptTo(Node.class);
                if (node != null) {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    final DateValue dateValue = new DateValue(calendar);
                    node.setProperty("eventCreated", dateValue);
                    resolver.commit();
                }
            }
        } catch (LoginException | RepositoryException | PersistenceException e) {
            logger.error(e.getMessage(), e);
            return JobResult.FAILED;
        }
        return JobResult.OK;
    }
}