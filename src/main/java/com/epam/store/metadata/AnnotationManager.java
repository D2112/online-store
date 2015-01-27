package com.epam.store.metadata;

import com.epam.store.action.WebAction;
import org.reflections.Reflections;

import java.util.Set;

public class AnnotationManager {

    public AnnotationManager() throws IllegalAccessException, InstantiationException {
        Reflections reflections = new Reflections("com.epam.store.action");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(WebAction.class);

        for (Class<?> actionClass : annotated) {
            WebAction annotation = actionClass.getAnnotation(WebAction.class);
            String path = annotation.path();
            Object o = actionClass.newInstance();
        }
    }
}
