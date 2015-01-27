package com.epam.store.filter;

import com.epam.store.model.Role;
import com.epam.store.model.User;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "SecurityFilter", servletNames = {"Controller"}, dispatcherTypes = DispatcherType.FORWARD)
public class SecurityFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(SecurityFilter.class);
    public static final String ADMIN_PAGE_PREFIX = "/admin";
    public static final String USER_PAGE_PREFIX = "/user";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        WebContext webContext = new WebContext(servletRequest, servletResponse);
        String path = webContext.getPathInfo();
        User user = (User) webContext.getAttribute("user", Scope.SESSION);
        log.debug("Path: " + path);
        log.debug("User: " + user);

        //if path is not secret and available for all
        if (!path.startsWith(ADMIN_PAGE_PREFIX) && !path.startsWith(USER_PAGE_PREFIX)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (user != null) {
            log.debug("User role: " + user.getRole().getName());
            String roleName = user.getRole().getName();
            if (!path.startsWith(ADMIN_PAGE_PREFIX) || roleName.equalsIgnoreCase(Role.ADMIN_ROLE_NAME)) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        webContext.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
