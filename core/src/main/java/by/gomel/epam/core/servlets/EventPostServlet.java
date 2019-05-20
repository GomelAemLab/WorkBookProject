package by.gomel.epam.core.servlets;

import by.gomel.epam.core.beans.Event;
import by.gomel.epam.core.validation.EventJsonValidation;
import by.gomel.epam.core.validation.ValidationError;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Event post servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/event"
        })
public class EventPostServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUid = 1L;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {
        try (final BufferedReader reader = req.getReader()) {

            final String json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            final Gson gson = new Gson();
            final Event event = gson.fromJson(json, Event.class);
            new EventJsonValidation(event).validate();
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (ValidationError e) {
            resp.sendError(e.getStatus());
        }
    }
}

