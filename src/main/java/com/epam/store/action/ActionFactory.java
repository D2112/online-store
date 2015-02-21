package com.epam.store.action;

import com.epam.store.config.ApplicationSettings;
import com.epam.store.config.PageConfig;
import com.epam.store.metadata.AnnotationManager;
import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ActionFactory {
    private static final Logger log = LoggerFactory.getLogger(ActionFactory.class);
    private static final String ACTIONS_PACKAGE = "com.epam.store.action";
    private static ActionFactory instance = new ActionFactory();
    private Map<String, Action> actions;

    static public ActionFactory getInstance() {
        return instance;
    }

    private ActionFactory() {
        PageConfig pageConfig = ApplicationSettings.getPageConfig();
        actions = initializeWebActions(pageConfig);
        mapStartPage(pageConfig.getStartPage());
    }

    public Action getAction(WebContext webContext) {
        Action action = actions.get(webContext.getRequestedAction());
        if (action == null) {
            log.debug("Action not found");
            action = new ShowPageAction(webContext.getPagePathFromURN()); //Try to show requested page
        }
        return action;
    }

    private Map<String, Action> initializeWebActions(PageConfig pageConfig) {
        Map<String, Action> actionsMap = new HashMap<>();
        Set<Class<?>> annotated = AnnotationManager.getAnnotatedClasses(ACTIONS_PACKAGE, WebAction.class);
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
        //get unannotated uri-paths and map them to default show page action
        Map<String, String> uriByPageName = pageConfig.getUriByJspPageNameMap();
        for (Map.Entry<String, String> entry : uriByPageName.entrySet()) {
            String pageName = entry.getKey();
            String keyPath = "GET/" + entry.getValue();
            actionsMap.put(keyPath, new ShowPageAction(pageName));
            log.debug("Path: " + keyPath + ", mapped with default show page action");
        }
        return actionsMap;
    }

    private void mapStartPage(String pageName) {
        actions.put("GET/", new ShowPageAction(pageName));
    }
}
