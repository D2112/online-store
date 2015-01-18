package com.epam.store.action;


import com.epam.store.servlet.WebContext;

public interface Action {
    public ActionResult execute(WebContext webContext);
}
