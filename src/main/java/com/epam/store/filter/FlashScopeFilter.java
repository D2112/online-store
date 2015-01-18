package com.epam.store.filter;


import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "FlashScope",
        servletNames = {"Controller", "ErrorHandler"}, dispatcherTypes = DispatcherType.FORWARD)
public class FlashScopeFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(FlashScopeFilter.class);

    @Override
    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        WebContext webContext = new WebContext(servletRequest, servletResponse);
        List<String> attributeNames = webContext.getAttributeNames(Scope.FLASH);
        if (attributeNames.size() > 0) {
            for (String attributeName : attributeNames) {
                Object attribute = webContext.getAttribute(attributeName, Scope.FLASH);
                log.debug("Adding attribute {} from flash scope to request", attributeName);
                webContext.setAttribute(attributeName, attribute, Scope.REQUEST);
                webContext.removeAttribute(attributeName, Scope.FLASH);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void destroy() {

    }
}
