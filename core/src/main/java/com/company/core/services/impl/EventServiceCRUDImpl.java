package com.company.core.services.impl;

import com.company.core.beans.event.EventHelper;
import com.company.core.configuration.UserPrincipal;
import com.company.core.execption.HttpException;
import com.company.core.execption.JcrException;
import com.company.core.execption.NotFoundException;
import com.company.core.models.Event;
import com.company.core.services.EventServiceCRUD;
import com.company.core.validation.DateHelper;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.search.QueryBuilder;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

import static com.company.core.constants.Constants.*;
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
    public String create(String path, Event event) throws JcrException {

        ResourceResolver resolver;
        Session session = null;
        String nodeName;
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
            session = resolver.adaptTo(Session.class);
            if (session == null) {
                throw new JcrException();
            }
            final Node node = JcrUtil.createPath(path, EVENT_FOLDER_TYPE, EVENT_FOLDER_TYPE, session, false);
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
    public Event getEvent(String eventPath) throws NotFoundException, JcrException {
        Event event;
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {
            Resource resource = resolver.getResource(eventPath);
            if (resource == null) {
                throw new NotFoundException();
            }
            event = resource.adaptTo(Event.class);
            event.setId(eventPath);
        } catch (LoginException e) {
            throw new JcrException();
        }
        return event;
    }

    @Override
    public List<Event> getEvents(String userName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Event> getEventsFromDate(String date) throws HttpException {
        throw new UnsupportedOperationException();
    }

    private void addEventsToList(List<Event> eventList, Resource resourceDateFolder) {
        resourceDateFolder.getChildren().forEach(resource -> {
                    final Event ev = resource.adaptTo(Event.class);
                    if (ev != null) {
                        DateHelper dateHelper = new DateHelper(ev.getEventDate());
                        ev.setId(dateHelper.getDatePath() + FOLDER_SEPARATOR + resource.getName());
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
    public void update(Event event) throws JcrException, NotFoundException {
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)) {

            final String path = event.getId();
            final Resource resource = resolver.getResource(path);
            if (resource == null) {
                throw new NotFoundException();
            }
            final Node eventNode = resource.adaptTo(Node.class);
            new EventHelper().setPropertiesToNode(event, eventNode);

            resolver.commit();
        } catch (LoginException | RepositoryException | PersistenceException e) {
            throw new JcrException(e.getMessage());
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
            Resource resource = resolver.getResource(EVENT_PATH + id);
            if (resource == null) {
                throw new NotFoundException();
            }
            Node event = resource.adaptTo(Node.class);
            event.remove();
            resolver.commit();
        } catch (LoginException | RepositoryException | PersistenceException e) {
            throw new JcrException();
        }
    }
}
