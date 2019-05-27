package by.gomel.epam.core.services.impl;

import by.gomel.epam.core.beans.Event.EventAdaptToNode;
import by.gomel.epam.core.execption.HttpException;
import by.gomel.epam.core.execption.JcrException;
import by.gomel.epam.core.execption.NotFoundException;
import by.gomel.epam.core.models.Event;
import by.gomel.epam.core.services.EventServiceCRUD;
import by.gomel.epam.core.validation.DateHelper;
import com.day.cq.commons.jcr.JcrUtil;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.*;

import static by.gomel.epam.core.constants.Constants.EVENT_FOLDER_TYPE;
import static by.gomel.epam.core.constants.Constants.EVENT_PATH;
import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED;

@Component(service = EventServiceCRUD.class)
public class EventServiceCRUDImpl implements EventServiceCRUD {

    private final Map<String, Object> param = new HashMap<>();

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Activate
    @Modified
    protected final void activate() {
        param.put(ResourceResolverFactory.SUBSERVICE, "serviceCRUD");
    }

    @Override
    public String create(Event event) throws JcrException {

        ResourceResolver resolver;
        Session session = null;
        String nodeName;
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
            session = resolver.adaptTo(Session.class);
            if(session == null){
                throw new JcrException();
            }
            final Node node = JcrUtil.createPath(event.getEventFolderPath(), EVENT_FOLDER_TYPE, EVENT_FOLDER_TYPE, session, false);
            nodeName = UUID.randomUUID().toString();
            Node eventNode = node.addNode(nodeName, NT_UNSTRUCTURED);
            new EventAdaptToNode(event).adaptTo(eventNode);
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
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)){

            final Resource resourceDateFolder = resolver.getResource(path);
            if (resourceDateFolder == null) {
                throw new NotFoundException();
            }
            setListEvents(eventList, resourceDateFolder);
        } catch (LoginException e) {
            throw new JcrException();
        }
        return eventList;
    }

    private void setListEvents(List<Event> eventList, Resource resourceDateFolder) {
        resourceDateFolder.getChildren().forEach(resource -> {
                    final Event ev = resource.adaptTo(Event.class);
                    if (ev != null) {
                        eventList.add(ev);
                    }
                }
        );
    }

    @Override
    public List<Event> getEvents() throws JcrException {

        List<Event> eventList = new ArrayList<>();
        try (ResourceResolver resolver = resolverFactory.getServiceResourceResolver(param)){

            final Resource eventsFolder = resolver.getResource(EVENT_PATH);
            if (eventsFolder == null) {
                return eventList;
            }
            Iterable<Resource> years = eventsFolder.getChildren();

            years.forEach(year -> {
                Iterable<Resource> months = year.getChildren();
                months.forEach(month -> {
                    Iterable<Resource> days = month.getChildren();
                    days.forEach(day -> {
                        setListEvents(eventList, day);
                    });
                });
            });
        } catch (LoginException e) {
            throw new JcrException();
        }
        return eventList;
    }

    @Override
    public Event update(String eventPath) {
        return null;
    }

    @Override
    public boolean delete(String eventPath) {
        return false;
    }


}
