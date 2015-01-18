package com.epam.store.filter;

import com.epam.store.servlet.WebContext;
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
        WebContext webContext = new WebContext(servletRequest, servletResponse);
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.startsWith("/static/")) {
            log.debug("Filtered static resource: " + path);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            log.debug("Forward to controller: " + path);
            req.getRequestDispatcher("/controller" + path).forward(req, servletResponse);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void destroy() {

    }
}
