package com.epam.store.filter;

import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

@WebFilter(filterName = "LanguageFilter", servletNames = "Controller", dispatcherTypes = DispatcherType.FORWARD)
public class LanguageFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(LanguageFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("Lang filter");
        WebContext webContext = new WebContext(servletRequest, servletResponse);
        HttpSession session = webContext.getSession();
        if (session.isNew()) {
            Cookie lang = webContext.findCookie("lang");
            if (lang != null) {
                String value = lang.getValue();
                Locale localeFromCookie = new Locale(value);
                webContext.setLocale(localeFromCookie);
            }
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
