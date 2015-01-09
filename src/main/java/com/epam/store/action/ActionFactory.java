package com.epam.store.action;

import com.epam.store.controller.Context;

import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    static private ActionFactory instance = new ActionFactory();
    private Map<String, Action> actions;

    static public ActionFactory getInstance() {
        return instance;
    }

    private ActionFactory() {
        actions = new HashMap<>();
        actions.put("GET/", new ShowPageAction("home"));
        actions.put("GET/home", new ShowPageAction("home"));
        actions.put("GET/signup", new ShowPageAction("signup"));
        actions.put("GET/registration", new ShowPageAction("registration"));
        actions.put("POST/registration", new RegistrationAction());
        actions.put("POST/signup", new LoginAction());
    }

    public Action getAction(Context context) {
        return actions.get(context.getRequestedPage());
    }
}
