package com.epam.store;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class Test {
    private static final String USER_EMAIL_COLUMN = "EMAIL";
    private static final String ROLE_NAME_COLUMN = "NAME";
    private static final String ROLE_ID_COLUMN = "ROLE_ID";
    private static final String USER_ID_COLUMN = "USER_ID";
    private static final String DATE_TIME_COLUMN = "TIME";
    private static final String STATUS_NAME_COLUMN = "NAME";
    private static final String FLASH_ATTRIBUTE_PREFIX = "flash.";

    public static void main(String[] args) throws SQLException {
        List<String> attributeNames = new ArrayList<>();
        attributeNames.add("flash.1");
        attributeNames.add("flash.2");
        attributeNames.add("flash.3");
        attributeNames.add("flash.4");
        attributeNames.add("notflash.1");
        attributeNames.add("notflash.2");
        attributeNames.add("notflash.3");
        attributeNames.add("notflash.4");
        System.out.println(getAttributeNamesForFlashScope(attributeNames));

    }

    private static void method(int i) {
        assert (i > 0) : i;
        System.out.println(i);
    }

    private static List<String> takeAttributeNamesFromFlashScope(List<String> enumeration) {
        List<String> attributeNames = new ArrayList<>(enumeration);
        //get names with flash-prefix
        Iterator<String> iterator = attributeNames.iterator();
        while (iterator.hasNext()) {
            String attributeName = iterator.next();
            if (!attributeName.startsWith(FLASH_ATTRIBUTE_PREFIX)) {
                iterator.remove();
            }
        }
        //remove flash-prefix from all names
        attributeNames.replaceAll(s -> s.substring(FLASH_ATTRIBUTE_PREFIX.length()));
        return attributeNames;
    }

    private static List<String> getAttributeNamesForFlashScope(List<String> enumeration) {
        List<String> attributeNames = new ArrayList<>(enumeration);
        for (String attributeName : attributeNames) {
            if (!attributeName.startsWith(FLASH_ATTRIBUTE_PREFIX)) {
                attributeNames.remove(attributeName);
            }
        }
        attributeNames.replaceAll(s -> s.substring(FLASH_ATTRIBUTE_PREFIX.length()));
        return attributeNames;
    }

    private <T> List<T> getListFromEnumeration(Enumeration<T> e) {
        List<T> list = new ArrayList<>();
        while (e.hasMoreElements()) {
            T element = e.nextElement();
            list.add(element);
        }
        return list;
    }
}
