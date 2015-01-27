package com.epam.store.action;

import com.epam.store.servlet.WebContext;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActionFactory {
    private static final String ACTIONS_PACKAGE = "com.epam.store.action";
    private static final String START_PAGE = "catalog";
    private static ActionFactory instance = new ActionFactory();
    private Map<String, Action> actions;

    static public ActionFactory getInstance() {
        return instance;
    }

    private ActionFactory() {
        actions = initializeActions();
        //Map start page
        actions.put("GET/", new ShowPageAction(START_PAGE));
    }

    public Action getAction(WebContext webContext) {
        Action action = actions.get(webContext.getRequestedAction());
        if (action == null) action = new ShowPageAction(webContext.getPagePathFromURI());
        return action;
    }

    private Map<String, Action> initializeActions() {
        Map<String, Action> actionsMap = new HashMap<>();
        Reflections reflections = new Reflections(ACTIONS_PACKAGE);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(WebAction.class);
        for (Class<?> actionClass : annotated) {
            WebAction annotation = actionClass.getAnnotation(WebAction.class);
            String path = annotation.path();
            Action actionObject;
            try {
                actionObject = (Action) actionClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ActionException("Error during Action Factory initialization", e);
            }
            actionsMap.put(path, actionObject);
        }
        return actionsMap;
    }
}
