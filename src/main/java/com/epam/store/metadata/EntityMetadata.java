package com.epam.store.metadata;


import com.epam.store.model.BaseEntity;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EntityMetadata<T extends BaseEntity> {
    private static final Logger log = LoggerFactory.getLogger(EntityMetadata.class);
    private Map<String, Method> setterByFieldName;
    private Map<String, Method> getterByFieldName;
    private List<String> fieldsNames;
    private Class<T> type;

    public EntityMetadata(Class<T> type) {
        fieldsNames = new ArrayList<>();
        setterByFieldName = new LinkedHashMap<>();
        getterByFieldName = new LinkedHashMap<>();
        this.type = type;

        addFieldsNamesFromFields(type.getSuperclass().getDeclaredFields()); //getting names from superclass
        addFieldsNamesFromFields(type.getDeclaredFields());

        addSetterAndGettersFromMethods(type.getSuperclass().getDeclaredMethods());//getting methods names from superclass
        addSetterAndGettersFromMethods(type.getDeclaredMethods());
    }

    public Class<T> getFieldType(String fieldName) {
        try {
            Class clazz = type.getDeclaredField(fieldName).getType();
            if (BaseEntity.class.isAssignableFrom(clazz)) {
                return clazz;
            }
        } catch (NoSuchFieldException e) {
            log.error("Exception while getting field type from entity: ", e);
            throw new MetadataException(e);
        }
        return null;
    }

    public Class<T> getEntityClass() {
        return type;
    }

    public boolean hasField(String fieldName) {
        return fieldsNames.contains(fieldName);
    }

    public List<String> getFieldsNames() {
        return ImmutableList.copyOf(fieldsNames);
    }

    public void invokeSetterByFieldName(String fieldName, Object targetToInvoke, Object... args) {
        try {
            setterByFieldName.get(fieldName).invoke(targetToInvoke, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MetadataException(e);
        }
    }

    public Object invokeGetterByFieldName(String fieldName, Object targetToInvoke) {
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
