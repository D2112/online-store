package com.epam.store.action;


import com.epam.store.servlet.Context;

public interface Action {
    public ActionResult execute(Context context);
}
