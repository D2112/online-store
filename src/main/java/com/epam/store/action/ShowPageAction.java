package com.epam.store.action;

import com.epam.store.servlet.WebContext;

public class ShowPageAction implements Action {
    private ActionResult result;

    public ShowPageAction(String page) {
        result = new ActionResult(page);
    }

    @Override
    public ActionResult execute(WebContext webContext) {
        return result;
    }
}
