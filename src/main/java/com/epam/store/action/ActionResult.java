package com.epam.store.action;

import java.util.Map;

public class ActionResult {
    private String page;
    private boolean redirect;
    private Map<String, String[]> parameterMap;

    public ActionResult(String page) {
        this.page = page;
    }

    public ActionResult(String page, boolean redirect) {
        this.page = page;
        this.redirect = redirect;
    }

    public ActionResult(String page, boolean redirect, Map<String, String[]> parameterMap) {
        this.page = page;
        this.redirect = redirect;
        this.parameterMap = parameterMap;
    }

    public String getPageName() {
        return page;
    }

    public boolean isRedirect() {
        return redirect;
    }
}
