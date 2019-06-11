package com.company.core.schedulers;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.ItemExistsException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.company.core.constants.Constants.*;

public class MoveToArchive implements Runnable {

    private final Map<String, Object> param;
    private final QueryBuilder queryBuilder;
    private final String pathToArchive;
    private final ResourceResolverFactory resolverFactory;
    private final Map<String, String> queryMap = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    MoveToArchive(String pathToArchive, String upperBound, Map<String, Object> param, QueryBuilder queryBuilder, ResourceResolverFactory resolverFactory) {
        this.pathToArchive = pathToArchive;
        this.param = param;
        this.queryBuilder = queryBuilder;
        this.resolverFactory = resolverFactory;
        queryMap.put("path", EVENT_PATH);
        queryMap.put("type", JcrConstants.NT_UNSTRUCTURED);
        queryMap.put("daterange.property", "eventCreated");
        queryMap.put("daterange.upperBound", upperBound);
    }

    @Override
    public void run() {
        logger.debug("the scheduler tries to execute");
        ResourceResolver resolver;
        Session session = null;
        String path = null;

        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
            session = resolver.adaptTo(Session.class);
            if (session != null) {

                final String folderToMove = JcrUtil.createPath(pathToArchive, EVENT_FOLDER_TYPE, EVENT_FOLDER_TYPE, session, false).getPath();
                final Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
                final List<Hit> resultHits = query.getResult().getHits();
                for (final Hit hit : resultHits) {
                    path = hit.getPath();
                    if (!path.contains(pathToArchive)) {
                        session.move(path, folderToMove + "/" + hit.getNode().getName());
                    }
                }
                session.save();
            }
        } catch (ItemExistsException e) {
            logger.error(e.getMessage());
            try {
                session.removeItem(path);
                session.save();
            } catch (RepositoryException e1) {
                logger.error("can't remove the duplicate node - '{}'", path);
            }
        } catch (RepositoryException | LoginException e) {
            logger.error(e.getMessage());
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
        }
    }
}
//TODO Query (Query Builder)
