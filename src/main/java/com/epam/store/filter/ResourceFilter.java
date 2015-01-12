package com.epam.store.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "ResourceFilter", urlPatterns = "/*", dispatcherTypes = DispatcherType.REQUEST)
public class ResourceFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(ResourceFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String path = req.getRequestURI().substring(req.getContextPath().length());
        log.debug("path in resource filter: " + path);

        if (path.startsWith("/static/")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            req.getRequestDispatcher("/controller/" + path).forward(servletRequest, servletResponse);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }
    public void destroy() {

    }
}
