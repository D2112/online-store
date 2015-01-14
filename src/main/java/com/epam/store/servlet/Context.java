package com.epam.store.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Context {
    private static final Logger log = LoggerFactory.getLogger(Context.class);
    private static final String FLASH_ATTRIBUTE_PREFIX = "flash.";
    private HttpServletRequest req;
    private HttpServletResponse resp;

    public Context(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;
    }

    public Context(ServletRequest servletRequest, ServletResponse servletResponse) {
        req = (HttpServletRequest) servletRequest;
        resp = (HttpServletResponse) servletResponse;
    }

    public String getRequestedAction() {
        return req.getMethod() + req.getPathInfo();
    }

    public String getParameter(String parameterName) {
        return req.getParameter(parameterName);
    }

    public String[] getParameterValues(String name) {
        return req.getParameterValues(name);
    }

    public Map<String, String[]> getParameterMap() {
        return req.getParameterMap();
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

    public Object getAttribute(String name, Scope scope) {
        Object attributeObject = null;
        switch (scope) {
            case REQUEST:
                attributeObject = req.getAttribute(name);
                break;
            case SESSION:
                if (isSessionExist()) {
                    attributeObject = req.getSession().getAttribute(name);
                }
                break;
            case APPLICATION:
                attributeObject = req.getServletContext().getAttribute(name);
                break;
            case FLASH:
                if (isSessionExist()) {
                    attributeObject = req.getSession().getAttribute(getFlashAttributeName(name));
                }
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
                if (isSessionExist()) {
                    req.getSession().removeAttribute(name);
                }
                break;
            case APPLICATION:
                req.getServletContext().removeAttribute(name);
                break;
            case FLASH:
                if (isSessionExist()) {
                    req.getSession().removeAttribute(getFlashAttributeName(name));
                }
                break;
        }
    }

    public List<String> getAttributeNames(Scope scope) {
        List<String> attributeNames = new ArrayList<>();
        switch (scope) {
            case REQUEST:
                attributeNames = getListFromEnumeration(req.getAttributeNames());
                break;
            case SESSION:
                if (isSessionExist()) {
                    attributeNames = getListFromEnumeration(req.getSession().getAttributeNames());
                }
                break;
            case APPLICATION:
                attributeNames = getListFromEnumeration(req.getServletContext().getAttributeNames());
                break;
            case FLASH:
                if (isSessionExist()) {
                    Enumeration<String> attributeEnumeration = req.getSession().getAttributeNames();
                    attributeNames = getAttributeNamesForFlashScope(attributeEnumeration);
                }
                break;
        }
        return attributeNames;
    }

    public void forward(String path) throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }

    public void sendRedirect(String location) throws IOException {
        resp.sendRedirect(location);
    }

    public String getContextPath() {
        return req.getContextPath();
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        return (T) getAttribute(serviceClass.getSimpleName(), Scope.APPLICATION);
    }

    public String getPreviousURI() {
        return req.getHeader("Referer").substring(req.getContextPath().length());
    }

    public String getURI() {
        return req.getRequestURI().substring(req.getContextPath().length());
    }

    public String getURIWithQueryString() {
        String queryString = req.getQueryString();
        String uri = getURI();
        if (queryString != null) uri += "?" + queryString;
        return uri;
    }

    private String getFlashAttributeName(String attributeName) {
        return FLASH_ATTRIBUTE_PREFIX + attributeName;
    }

    private boolean isSessionExist() {
        return req.getSession(false) != null;
    }

    private <T> List<T> getListFromEnumeration(Enumeration<T> e) {
        List<T> list = new ArrayList<>();
        while (e.hasMoreElements()) {
            T element = e.nextElement();
            list.add(element);
        }
        return list;
    }

    /**
     * Remove all names without flash prefix from the list,
     * and then remove flash prefix of the remaining names
     *
     * @return List of flash attribute names without flash prefix
     */
    private List<String> getAttributeNamesForFlashScope(Enumeration<String> enumeration) {
        List<String> attributeNames = new CopyOnWriteArrayList<>(getListFromEnumeration(enumeration));

        for (String attributeName : attributeNames) {
            if (!attributeName.startsWith(FLASH_ATTRIBUTE_PREFIX)) {
                attributeNames.remove(attributeName);
            }
        }
        attributeNames.replaceAll(s -> s.substring(FLASH_ATTRIBUTE_PREFIX.length()));
        return attributeNames;
    }
}

