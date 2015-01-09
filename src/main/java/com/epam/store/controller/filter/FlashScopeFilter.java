package com.epam.store.controller.filter;



import com.epam.store.controller.Scope;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

public class FlashScopeFilter implements Filter {
    @Override
    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession(false);
        if(session != null) {
            Enumeration<String> attributeNames = session.getAttributeNames();
            while(attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                if(attributeName.startsWith(Scope.FLASH.name() + ".")) {
                   //req.setAttribute( ,session.getAttribute(attributeName));
                }
            }

         /*   if (flashParams != null) {
                for (Map.Entry<String, Object> flashEntry : flashParams.entrySet()) {
                    req.setAttribute(flashEntry.getKey(), flashEntry.getValue());
                }
                session.removeAttribute(FLASH_SESSION_KEY);
            }*/
        }
        filterChain.doFilter(req, resp);



    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
