package com.company.core.schedulers;

import com.company.core.configuration.UserPrincipal;
import com.day.cq.search.QueryBuilder;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.company.core.constants.Constants.JOB_ARCHIVE;

@Designate(ocd = CronScheduler.Config.class)
@Component
public class CronScheduler {

    private final Map<String, Object> param = new HashMap<>();

    @Reference
    private Scheduler scheduler;

    @Reference
    private QueryBuilder queryBuilder;


    @Reference
    private ResourceResolverFactory resolverFactory;
    @Reference
    private UserPrincipal userPrincipal;

    @ObjectClassDefinition(name = "Work book schedule events",
            description = "values for an archiving and Cron-job expression ")
    public @interface Config {

        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "0 0 0 * * ?";

        @AttributeDefinition(name = "Number of days to start  archiving")
        int minus_days() default 30;

    }

    @Activate
    @Modified
    protected void activate(final Config config) {
        scheduler.unschedule(JOB_ARCHIVE);
        param.put(ResourceResolverFactory.SUBSERVICE, userPrincipal.getUserMappingPrincipal());
        final String minusDaysInMillisec = LocalDate.now().minusDays(config.minus_days()).toString();
        scheduler.schedule(new MoveToArchive(minusDaysInMillisec, param, queryBuilder, resolverFactory),
                scheduler.EXPR(config.scheduler_expression()).name(JOB_ARCHIVE));
    }

    @Deactivate
    protected void deactivate() {
        scheduler.unschedule(JOB_ARCHIVE);
    }
}
//TODO Job + CRON - get statistic by created events in period
