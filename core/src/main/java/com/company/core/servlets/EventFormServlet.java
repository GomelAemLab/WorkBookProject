package com.company.core.servlets;

import com.company.core.beans.event.EventHelper;
import com.company.core.execption.JcrException;
import com.company.core.execption.NotFoundException;
import com.company.core.execption.ValidationError;
import com.company.core.models.Event;
import com.company.core.services.EventServiceCRUD;
import com.company.core.validation.EventValidation;
import com.google.common.base.Strings;
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
import java.io.IOException;
import java.util.Map;

import static com.company.core.constants.Constants.*;

@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=event post servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_POST,
                "sling.servlet.paths=" + "/bin/event",
                "sling.servlet.selectors=" + FORM_SELECTOR
        })
public class EventFormServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUid = 1L;
    private static final String EXT_HTML = ".html";

    @Reference
    private EventServiceCRUD serviceCRUD;

    protected boolean processed(final SlingHttpServletRequest req,
                                final SlingHttpServletResponse resp) throws ServletException, IOException {
        boolean processed = false;
        final String operation = req.getParameter(OPERATION);
        if (Strings.isNullOrEmpty(operation)) {
            return false;
        }
        switch (operation.toLowerCase().trim()) {
            case "delete": {
                doDelete(req, resp);
                processed = true;
                break;
            }
            case "put": {
                doPut(req, resp);
                processed = true;
                break;
            }
        }
        return processed;
    }

    @Override
    protected void doPost(final SlingHttpServletRequest req,
                          final SlingHttpServletResponse resp) throws ServletException, IOException {

        try {
            if (processed(req, resp)) {
                return;
            }
            Map<String, String[]> parameterMap = req.getParameterMap();
            EventHelper helper = new EventHelper();
            Event event = helper.fromMap(parameterMap);
            String pathToRedirect = parameterMap.get(REDIRECT_PATH)[0];
            new EventValidation(event).validate();
            serviceCRUD.create(event);
            redirect(resp, pathToRedirect);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JcrException | ValidationError e) {
            resp.sendError(e.getStatusCode(), e.getMessage());
        }
    }

    @Override
    protected void doPut(final SlingHttpServletRequest req,
                         final SlingHttpServletResponse resp) throws ServletException, IOException {
        try {

            Map<String, String[]> parameterMap = req.getParameterMap();
            EventHelper helper = new EventHelper();
            Event event = helper.fromMap(parameterMap);
            String pathToRedirect = parameterMap.get(REDIRECT_PATH)[0];
            new EventValidation(event).validate();
            serviceCRUD.update(event);
            redirect(resp, pathToRedirect);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JcrException | ValidationError | NotFoundException e) {
            resp.sendError(e.getStatusCode(), e.getMessage());
        }
    }

    @Override
    protected void doDelete(final SlingHttpServletRequest req,
                            final SlingHttpServletResponse resp) throws ServletException, IOException {
        try {
            final String id = req.getParameter(ID);
            final String pathToRedirect = req.getParameter(REDIRECT_PATH);
            serviceCRUD.delete(id);
            redirect(resp, pathToRedirect);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (JcrException | NotFoundException e) {
            resp.sendError(e.getStatusCode(), e.getMessage());
        }
    }

    private void redirect(final SlingHttpServletResponse resp, String pathToRedirect) throws IOException {
        if (!Strings.isNullOrEmpty(pathToRedirect)) {
            resp.sendRedirect(pathToRedirect + EXT_HTML);
        }
    }
}


