package com.company.core.filters;

import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.engine.EngineConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

import static com.company.core.constants.Constants.*;

@Component(service = Filter.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "=event post filter",
                EngineConstants.SLING_FILTER_SCOPE + EQUALITY_SYMBOL + EngineConstants.FILTER_SCOPE_REQUEST,
                EngineConstants.SLING_FILTER_PATTERN + EQUALITY_SYMBOL + "/bin/event/form",
                "sling.filter.methods" + EQUALITY_SYMBOL + HttpConstants.METHOD_POST,
                Constants.SERVICE_RANKING + ":Integer" + EQUALITY_SYMBOL + Integer.MAX_VALUE
        })
public class FormErrorRedirectFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        chain.doFilter(request, response);

        SlingHttpServletResponse slingHttpServletResponse = (SlingHttpServletResponse) response;

        if (slingHttpServletResponse.getStatus() == HttpStatus.SC_UNPROCESSABLE_ENTITY) {
            String referer = ((SlingHttpServletRequest) request).getHeader(HEADER_REFERER);
            if (referer.contains(URL_DELIMITER_QUERY_PARAM)) {
                slingHttpServletResponse.sendRedirect(referer + URL_CONCAT_QUERY_PARAM + ERROR_MESSAGE_PARAMETER +
                        EQUALITY_SYMBOL + request.getAttribute(ERROR_MESSAGE_PARAMETER));
            } else {
                slingHttpServletResponse.sendRedirect(referer + URL_DELIMITER_QUERY_PARAM + ERROR_MESSAGE_PARAMETER +
                        EQUALITY_SYMBOL + request.getAttribute(ERROR_MESSAGE_PARAMETER));
            }

            logger.error("Request [{}] was redirected by reason: {}",
                    ((SlingHttpServletRequest) request).getRequestPathInfo().getResourcePath(),
                    request.getAttribute(ERROR_MESSAGE_PARAMETER));
        }
    }

    @Override
    public void destroy() {

    }
}
