package com.epam.store.action;

public class ActionResult {
    private String page;
    private boolean redirect;

    public ActionResult(String page) {
        this.page = page;
    }

    public ActionResult(String page, boolean redirect) {
        this.page = page;
        this.redirect = redirect;
    }

    public String getPageName() {
        return page;
    }

    public boolean isRedirect() {
        return redirect;
    }
}
