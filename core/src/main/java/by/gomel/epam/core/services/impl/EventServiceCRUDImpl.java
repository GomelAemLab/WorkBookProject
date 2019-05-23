package by.gomel.epam.core.services.impl;

import by.gomel.epam.core.beans.Event.Event;
import by.gomel.epam.core.beans.Event.EventAdaptToNode;
import by.gomel.epam.core.execption.JcrException;
import by.gomel.epam.core.services.EventServiceCRUD;
import com.day.cq.commons.jcr.JcrUtil;
import org.apache.sling.api.resource.LoginException;
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
import static com.day.cq.commons.jcr.JcrConstants.NT_UNSTRUCTURED;

@Component(service = EventServiceCRUD.class)
public class EventServiceCRUDImpl implements EventServiceCRUD {

    private final Map<String, Object> param = new HashMap<>();

    @Reference
    public ResourceResolverFactory resolverFactory;

    @Activate
    @Modified
    protected final void activate() {
        param.put(ResourceResolverFactory.SUBSERVICE, "serviceCRUD");
    }

    @Override
    public String create(Event event) throws JcrException {

        ResourceResolver resolver = null;
        Session session = null;
        String nodeName;
        try {
            resolver = resolverFactory.getServiceResourceResolver(param);
            session = resolver.adaptTo(Session.class);
            final Node node = JcrUtil.createPath(event.getEventFolderPath(), EVENT_FOLDER_TYPE, EVENT_FOLDER_TYPE, session, false);
            nodeName = UUID.randomUUID().toString();
            Node eventNode = node.addNode(nodeName, NT_UNSTRUCTURED);
            new EventAdaptToNode(event).adaptTo(eventNode);
            session.save();
        } catch (LoginException | RepositoryException e) {
            nodeName = null;
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
    public List<Event> getEvents(Date date) {
        return null;
    }

    @Override
    public List<Event> getEvents() {
        return null;
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
