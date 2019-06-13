package com.company.core.services.impl;

import com.company.core.beans.event.EventHelper;
import com.company.core.configuration.UserPrincipal;
import com.company.core.execption.HttpException;
import com.company.core.execption.JcrException;
import com.company.core.execption.NotFoundException;
import com.company.core.execption.ValidationError;
import com.company.core.models.Event;
import com.company.core.services.EventServiceCRUD;
import com.company.core.validation.DateHelper;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.google.common.base.Strings;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.company.core.constants.Constants.EMPTY_FIELD_ERROR_MSG;
import static com.company.core.constants.Constants.EVENT_FOLDER_TYPE;
import static com.company.core.constants.Constants.EVENT_PATH;
import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED;

@Component(service = EventServiceCRUD.class)

public class EventServiceCRUDImpl implements EventServiceCRUD {

    private final Map<String, Object> param = new HashMap<>();

    @Reference
    private ResourceResolverFactory resolverFactory;
    @Reference
    private UserPrincipal userPrincipal;
    @Reference
    private QueryBuilder queryBuilder;

    @Activate
    @Modified
    protected final void activate() {
        param.put(ResourceResolverFactory.SUBSERVICE, userPrincipal.getUserMappingPrincipal());
    }

    @Override
    public String create(Event event) throws JcrException {

        ResourceResolver resolver;
        Session session = null;
        String nodeName;
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
            session = resolver.adaptTo(Session.class);
            if (session == null) {
                throw new JcrException();
            }
            final Node node = JcrUtil.createPath(event.getEventFolderPath(), EVENT_FOLDER_TYPE, EVENT_FOLDER_TYPE, session, false);
            nodeName = UUID.randomUUID().toString();
            Node eventNode = node.addNode(nodeName, NT_UNSTRUCTURED);
            new EventHelper().setPropertiesToNode(event, eventNode);
            session.save();
        } catch (LoginException | RepositoryException e) {
            throw new JcrException();
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
        }
        return nodeName;
    }

    @Override
    public Event getEvent(String eventPath) {
        return null;
    }

    @Override
    public List<Event> getEvents(String userName) {
        return null;
    }

    @Override
    public List<Event> getEventsFromDate(String date) throws HttpException {
        DateHelper helper = new DateHelper(date);
        final String path = helper.validateAndGetPath();

        List<Event> eventList = new ArrayList<>();
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {

            final Resource resourceDateFolder = resolver.getResource(path);
            if (resourceDateFolder == null) {
                throw new NotFoundException();
            }
            addEventsToList(eventList, resourceDateFolder);
        } catch (LoginException e) {
            throw new JcrException();
        }
        return eventList;
    }

    private void addEventsToList(List<Event> eventList, Resource resourceDateFolder) {
        resourceDateFolder.getChildren().forEach(resource -> {
                    final Event ev = resource.adaptTo(Event.class);
                    if (ev != null) {
                        ev.setId(resource.getName());
                        eventList.add(ev);
                    }
                }
        );
    }

    @Override
    public List<Event> getEvents() throws JcrException {

        List<Event> eventList = new ArrayList<>();
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {

            final Resource eventsFolder = resolver.getResource(EVENT_PATH);
            if (eventsFolder == null) {
                return eventList;
            }
            Iterable<Resource> years = eventsFolder.getChildren();

            years.forEach(year -> {
                Iterable<Resource> months = year.getChildren();
                months.forEach(month -> {
                    Iterable<Resource> days = month.getChildren();
                    days.forEach(day -> addEventsToList(eventList, day));
                });
            });
        } catch (LoginException e) {
            throw new JcrException();
        }
        return eventList;
    }

    @Override
    public void update(Event event) throws JcrException, NotFoundException, ValidationError {
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {

            final String path = getAbsPath(event);
            final Resource resource = resolver.getResource(path);
            if (resource == null) {
                throw new NotFoundException();
            }
            final Node eventNode = resource.adaptTo(Node.class);
            new EventHelper().setPropertiesToNode(event, eventNode);
        } catch (LoginException | RepositoryException e) {
            throw new JcrException();
        }
    }

    @Override
    public List<Event> selectClosest(Calendar fromDate, int limit) throws JcrException {
        String formattedDate = fromDate.toInstant().toString();
        final String SQL2_SELECT_CLOSEST = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE([%s]) " +
                "AND s.[eventDate] IS NOT NULL AND s.[eventDate] > CAST('%s' AS DATE) ORDER BY s.[eventDate] ASC";
        List<Event> eventList = new ArrayList<>();
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {
            Iterator<Resource> resources = resolver
                    .findResources(String.format(SQL2_SELECT_CLOSEST, EVENT_PATH, formattedDate), javax.jcr.query.Query.JCR_SQL2);
            while (resources.hasNext() && (limit-- > 0)) {
                Event event = resources.next().adaptTo(Event.class);
                if (event != null) {
                    eventList.add(event);
                }
            }
        } catch (LoginException e) {
            throw new JcrException();
        }
        return eventList;
    }

    public void delete(String id) throws NotFoundException, JcrException {
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {
            final Session session = resolver.adaptTo(Session.class);
            if (session == null) {
                throw new RepositoryException();
            }
            final Map<String, String> queryMap = new HashMap<>();
            queryMap.put("path", EVENT_PATH);
            queryMap.put("type", JcrConstants.NT_UNSTRUCTURED);
            queryMap.put("nodename", id);
            final Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), session);
            final List<Hit> hits = query.getResult().getHits();
            if (!hits.isEmpty()) {
                int alwaysSingle = 0;
                session.removeItem(hits.get(alwaysSingle).getPath());
                session.save();
            } else {
                throw new PathNotFoundException();
            }
        } catch (PathNotFoundException e) {
            throw new NotFoundException();

        } catch (LoginException | RepositoryException e) {
            throw new JcrException();
        }
    }

    private String getAbsPath(Event event) throws ValidationError {
        final String id = event.getId();
        final String date = event.getEventDateHtmlFormat();
        if (Strings.isNullOrEmpty(id) || Strings.isNullOrEmpty(date)) {
            throw new ValidationError(EMPTY_FIELD_ERROR_MSG);

        }
        return EVENT_PATH + event.getEventDate() + event.getId();
    }
}
