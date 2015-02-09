package com.epam.store.metadata;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public class AnnotationManager {
    public static Set<Class<?>> getAnnotatedClasses(String packageName, Class<? extends Annotation> annotationClass) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(annotationClass);
    }
}
