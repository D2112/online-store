package com.epam.store.action;

import com.epam.store.config.PageConfig;
import com.epam.store.servlet.WebContext;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActionFactory {
    private static final Logger log = LoggerFactory.getLogger(ActionFactory.class);
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
        if (action == null) {
            log.debug("Action not found");
            action = new ShowPageAction(webContext.getPagePathFromURI()); //Try to show requested page
        }
        return action;
    }

    private Map<String, Action> initializeActions() {
        Map<String, Action> actionsMap = new HashMap<>();
        Reflections reflections = new Reflections(ACTIONS_PACKAGE);
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(WebAction.class);
        for (Class<?> actionClass : annotated) {
            WebAction annotation = actionClass.getAnnotation(WebAction.class);
            String[] paths = annotation.path();
            Action actionObject;
            try {
                actionObject = (Action) actionClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new ActionException("Error during Action Factory initialization", e);
            }
            for (String keyPath : paths) {
                log.debug("Path: " + keyPath + ", mapping with " + actionObject.getClass().getSimpleName());
                actionsMap.put(keyPath, actionObject);
            }
        }
        //get unannotated pages and map them to default shows page action
        HashMap<String, String> uriByPageName = PageConfig.getInstance().getUriByPageNameMap();
        for (Map.Entry<String, String> entry : uriByPageName.entrySet()) {
            String pageName = entry.getKey();
            String keyPath = "GET/" + entry.getValue();
            actionsMap.put(keyPath, new ShowPageAction(pageName));
            log.debug("Path: " + keyPath + ", mapped with default show page action");
        }
        return actionsMap;
    }
}
