package com.epam.store.controller.action;

import com.epam.store.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Context {
    private HttpServletRequest req;
    private HttpServletResponse resp;

    public Context(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;
    }

    public String getRequestedPage() {
        return req.getMethod() + req.getPathInfo();
    }

    public void setUserToSession(User user) {
        req.getSession().setAttribute("user", user);
    }

    public void setAttributeToRequest(String name, Object value) {
        req.setAttribute(name, value);
    }

    public String getParameter(String parameterName) {
        return req.getParameter(parameterName);
    }
}

