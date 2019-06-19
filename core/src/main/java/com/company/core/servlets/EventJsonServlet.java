package com.company.core.servlets;

import com.company.core.execption.JcrException;
import com.company.core.execption.ValidationError;
import com.company.core.models.Event;
import com.company.core.services.EventServiceCRUD;
import com.company.core.validation.DateHelper;
import com.company.core.validation.EventValidation;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.stream.Collectors;

import static com.company.core.constants.Constants.EVENT_PATH;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=event json servlet",
                "sling.servlet.paths=" + "/bin/event/json"
        })
public class EventJsonServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUid = 1L;

    @Reference
    private EventServiceCRUD serviceCRUD;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {

        try (BufferedReader reader = req.getReader()) {
            final String json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            final Gson gson = new Gson();
            final Event event = gson.fromJson(json, Event.class);
            final DateHelper dateHelper = new DateHelper(event.getEventDateHtmlFormat(), event.getEventTime());
            final Calendar date = dateHelper.getDate();
            event.setEventDate(date);
            new EventValidation(event).validate(true);

            serviceCRUD.create(EVENT_PATH + dateHelper.getDatePath(), event);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JcrException | ValidationError e) {
            resp.sendError(e.getStatusCode(), e.getMessage());
        }
    }
}


