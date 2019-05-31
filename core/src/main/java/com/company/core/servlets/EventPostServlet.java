package com.company.core.servlets;

import com.company.core.beans.event.EventHelper;
import com.company.core.execption.JcrException;
import com.company.core.execption.ValidationError;
import com.company.core.models.Event;
import com.company.core.services.EventServiceCRUD;
import com.company.core.validation.EventValidation;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static com.company.core.constants.Constants.FORM_SELECTOR;
import static com.company.core.constants.Constants.REDIRECT_PATH;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=event post servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/event"
        })
public class EventPostServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUid = 1L;

    @Reference
    private EventServiceCRUD serviceCRUD;

    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {

        BufferedReader reader = null;
        try {
            Event event;
            String pathToRedirect = null;
            if (FORM_SELECTOR.equals(req.getRequestPathInfo().getSelectorString())) {

                Map<String, String[]> parameterMap = req.getParameterMap();
                EventHelper helper = new EventHelper();
                event = helper.fromMap(parameterMap);
                pathToRedirect = parameterMap.get(REDIRECT_PATH)[0];

            } else {

                reader = req.getReader();
                final String json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                final Gson gson = new Gson();
                event = gson.fromJson(json, Event.class);
            }
            new EventValidation(event).validate();
            serviceCRUD.create(event);
            if (!Strings.isNullOrEmpty(pathToRedirect)) {
                resp.sendRedirect(pathToRedirect + ".html");
            }
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JcrException | ValidationError e) {
            resp.sendError(e.getStatusCode(), e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}


