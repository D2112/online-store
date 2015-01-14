package com.epam.store.filter;

import com.epam.store.servlet.Context;
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
        Context context = new Context(servletRequest, servletResponse);
        String path = context.getURI();
        log.debug("path in resource filter: " + path);

        if (path.startsWith("/static/")) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            context.forward("/controller/" + path);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void destroy() {

    }
}
