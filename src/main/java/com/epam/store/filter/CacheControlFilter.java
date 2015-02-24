package com.epam.store.filter;

import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "CacheControlFilter", dispatcherTypes = DispatcherType.REQUEST)
public class CacheControlFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(CacheControlFilter.class);
    private static final long MAX_AGE = 1000000; //one day
    private static final long EXPIRY = System.currentTimeMillis() + MAX_AGE; //one day

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        log.debug("CACHE FILTER");
        WebContext webContext = new WebContext(servletRequest, servletResponse);
        String path = webContext.getURI();

        if (path.startsWith("/static/") || path.startsWith("/image/")) {
            resp.setDateHeader("Expires", EXPIRY);
            resp.setHeader("Cache-Control", "max-age=" + MAX_AGE);
        } else {
            //otherwise disable caching
            resp.setHeader("Cache-Control", "private, no-cache, no-store, must-revalidate"); // HTTP 1.1.
            resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            resp.setDateHeader("Expires", 0); // Proxies.
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
