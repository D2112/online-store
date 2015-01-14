package com.epam.store.action;

import com.epam.store.servlet.Context;

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
        actions.put("GET/", new ShowPageAction("catalog"));
        actions.put("GET/catalog", new ShowCategoryAction());
        actions.put("GET/login", new ShowPageAction("login"));
        actions.put("GET/registration", new ShowPageAction("registration"));
        actions.put("GET/cart", new ShowPageAction("cart"));
        actions.put("POST/addToCart", new AddToCartAction());
        actions.put("POST/deleteFromCart", new DeleteFromCart());
        actions.put("POST/registration", new RegistrationAction());
        actions.put("POST/login", new LoginAction());
    }

    public Action getAction(Context context) {
        return actions.get(context.getRequestedAction());
    }
}
