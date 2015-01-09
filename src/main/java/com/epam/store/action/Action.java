package com.epam.store.action;


import com.epam.store.controller.Context;

public interface Action {
    public ActionResult execute(Context context);
}
