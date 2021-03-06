package com.epam.store.action;

public class Validator {
    private static final String NAME_REGEX = "[A-Za-z0-9А-Яа-я ]{3,16}";
    private static final String EMAIL_REGEX = "[A-Za-z0-9_]{3,24}@[A-Za-z]{2,16}.[A-Za-z]{2,6}";
    private static final String INTEGER_NUMBER_REGEX = "[0-9]+";
    private static final String DECIMAL_NUMBER_REGEX = "[0-9]+[.][0-9]+";
    private static final String MAX_NUMBER_SIZE_REGEX = ".{9}";
    private static final int MAX_DESCRIPTION_SIZE = 1024;

    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public static boolean isNameValid(String name) {
        return (name.matches(NAME_REGEX));
    }

    public static boolean isIntegerNumber(String s) {
        return s.matches(INTEGER_NUMBER_REGEX);
    }

    public static boolean isDecimalNumber(String str) {
        return str.matches(DECIMAL_NUMBER_REGEX);
    }

    public static boolean notNumber(String str) {
        return !Validator.isIntegerNumber(str) && !Validator.isDecimalNumber(str);
    }

    public static boolean isNumberTooLarge(String number) {
        return number.matches(MAX_NUMBER_SIZE_REGEX);
    }

    public static boolean isDescriptionTooBig(String description) {
        return description.length() > MAX_DESCRIPTION_SIZE;
    }
}
