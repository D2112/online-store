package com.epam.store.metadata;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Contains field names of certain entity and their types
 * Allows invoke entity setters and getters by name
 *
 * @param <T> type of entity
 */
public class EntityManager<T> {
    private static final Logger log = LoggerFactory.getLogger(EntityManager.class);
    private static final Map<Class, EntityManager> cache = new HashMap<>();
    private Map<String, Method> setterByFieldName;
    private Map<String, Method> getterByFieldName;
    private List<String> fieldsNames;
    private Class<T> type;

    @SuppressWarnings("unchecked")
    public static synchronized <T> EntityManager<T> getManager(Class<T> type) {
        EntityManager<T> entityManager = cache.get(type);
        if (entityManager == null) entityManager = new EntityManager<>(type);
        cache.put(type, entityManager);
        return entityManager;
    }

    private EntityManager(Class<T> type) {
        log.debug("initializing entity metadata");
        fieldsNames = new ArrayList<>();
        setterByFieldName = new LinkedHashMap<>();
        getterByFieldName = new LinkedHashMap<>();
        this.type = type;

        addFieldsNamesFromFields(type.getSuperclass().getDeclaredFields()); //getting names from superclass
        addFieldsNamesFromFields(type.getDeclaredFields());

        addSetterAndGettersFromMethods(type.getSuperclass().getDeclaredMethods()); //getting methods names from superclass
        addSetterAndGettersFromMethods(type.getDeclaredMethods());
    }

    public Class<?> getFieldType(String fieldName) {
        try {
            return type.getDeclaredField(fieldName).getType(); //getting type of the field by name
        } catch (NoSuchFieldException e) {
            String errorMessage = "Exception while getting field type from entity: ";
            log.error(errorMessage, e);
            throw new MetadataException(errorMessage, e);
        }
    }

    public Class<T> getEntityClass() {
        return type;
    }

    public boolean hasField(String fieldName) {
        return fieldsNames.contains(fieldName);
    }

    public List<String> getFieldsNames() {
        return fieldsNames;
    }

    /**
     * Invoke setter by field name in specified object
     * @param fieldName field name to find appropriate setter
     * @param targetToInvoke object for invoke setter
     * @param args parameters of the setter
     */
    public void invokeSetterByFieldName(String fieldName, Object targetToInvoke, Object... args) {
        try {
            setterByFieldName.get(fieldName).invoke(targetToInvoke, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MetadataException(e);
        }
    }

    /**
     * Invoke getter by field name in specified object
     * @param fieldName field name to find appropriate getter
     * @param targetToInvoke object for invoke getter
     */
    public Object invokeGetter(String fieldName, Object targetToInvoke) {
        try {
            return getterByFieldName.get(fieldName).invoke(targetToInvoke);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MetadataException(e);
        }
    }

    private String getFieldNameFromMethodName(String methodName) {
        StringBuilder sb = new StringBuilder();
        methodName = methodName.substring(3); //remove get or set prefix
        sb.append(Character.toLowerCase(methodName.charAt(0))); //turn first character into lowercase
        sb.append(methodName.substring(1)); //append rest part
        return sb.toString();
    }

    private void addFieldsNamesFromFields(Field[] fields) {
        for (Field field : fields) {
            fieldsNames.add(field.getName());
        }
    }

    private void addSetterAndGettersFromMethods(Method[] methods) {
        for (Method method : methods) {
            String methodName = method.getName();
            String fieldName = getFieldNameFromMethodName(methodName);
            if (methodName.startsWith("set")) {
                setterByFieldName.put(fieldName, method);
            }
            if (methodName.startsWith("get")) {
                getterByFieldName.put(fieldName, method);
            }
        }
    }
}
