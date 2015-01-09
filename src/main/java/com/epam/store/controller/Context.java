package com.epam.store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Context {
    private static final Logger log = LoggerFactory.getLogger(Context.class);
    private HttpServletRequest req;
    private HttpServletResponse resp;

    public Context(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;
    }

    public String getRequestedPage() {
        return req.getMethod() + req.getPathInfo();
    }

    public String getParameter(String parameterName) {
        return req.getParameter(parameterName);
    }

    public void setAttribute(String name, Object value, Scope scope) {
        switch (scope) {
            case REQUEST:
                req.setAttribute(name, value);
                break;
            case SESSION:
                req.getSession().setAttribute(name, value);
                break;
            case APPLICATION:
                req.getServletContext().setAttribute(name, value);
                break;
            case FLASH:
                req.getSession().setAttribute(getFlashAttributeName(name), value);
                break;
        }
    }

    public Object findAttribute(String name, Scope scope) {
        Object attributeObject = null;
        switch (scope) {
            case REQUEST:
                attributeObject = req.getAttribute(name);
                break;
            case SESSION:
                attributeObject = req.getSession(false).getAttribute(name);
                break;
            case APPLICATION:
                attributeObject = req.getServletContext().getAttribute(name);
                break;
            case FLASH:
                req.getAttribute(getFlashAttributeName(name));
                break;
        }
        return attributeObject;
    }


    public void removeAttribute(String name, Scope scope) {
        switch (scope) {
            case REQUEST:
                req.removeAttribute(name);
                break;
            case SESSION:
                req.getSession(false).removeAttribute(name);
                break;
            case APPLICATION:
                req.getServletContext().removeAttribute(name);
                break;
            case FLASH:
                req.getSession(false).removeAttribute(getFlashAttributeName(name));
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        return (T) findAttribute(serviceClass.getSimpleName(), Scope.APPLICATION);
    }

    private String getFlashAttributeName(String attributeName) {
        return Scope.FLASH.name() + "." + attributeName;
    }
}

