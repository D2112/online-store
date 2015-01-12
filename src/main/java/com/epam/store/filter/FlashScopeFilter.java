package com.epam.store.filter;



import com.epam.store.controller.Context;
import com.epam.store.controller.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebFilter(filterName = "FlashScope", servletNames = "Controller", dispatcherTypes = DispatcherType.FORWARD)
public class FlashScopeFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(FlashScopeFilter.class);

    @Override
    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        log.debug("Start filtering flash scope");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        Context context = new Context(req, resp);
        List<String> attributeNames = context.getAttributeNames(Scope.FLASH);
        if(attributeNames.size() > 0) {
            for (String attributeName : attributeNames) {
                Object attribute = context.findAttribute(attributeName, Scope.FLASH);
                log.debug("Adding attribute {} from flash scope to request", attributeName);
                context.setAttribute(attributeName, attribute, Scope.REQUEST);
                context.removeAttribute(attributeName, Scope.FLASH);
            }
        }
        filterChain.doFilter(req, resp);
    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }
    public void destroy() {

    }
}
