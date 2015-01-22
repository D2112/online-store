package com.epam.store.action;

public class RegexValidator {
    private static final String NAME_REGEX = "[A-Za-z0-9А-Яа-я_]{3,16}";
    private static final String EMAIL_REGEX = "[A-Za-z0-9_]{3,24}@[A-Za-z]{2,16}.[A-Za-z]{2,6}";
    private static final String INTEGER_NUMBER_REGEX = "[0-9]+";
    private static final String DECIMAL_NUMBER_REGEX = "[0-9]+[.^,][0-9]+";


    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public static boolean isNameValid(String name) {
        return (name.matches(NAME_REGEX));
    }

    public static boolean isIntegerNumber(String number) {
        return number.matches(INTEGER_NUMBER_REGEX);
    }

    public static boolean isDecimalNumber(String number) {
        return number.matches(DECIMAL_NUMBER_REGEX);
    }
}
