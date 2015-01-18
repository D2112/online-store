package com.epam.store.service;

public class InputValidator {
    private static final String NAME_REGEX = "[A-Za-z0-9А-Яа-я_]{3,16}";
    private static final String EMAIL_REGEX = "[A-Za-z0-9_]{3,24}@[A-Za-z]+.[A-Za-z]+";

    public static boolean isEmailValid(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public static boolean isNameValid(String name) {
        return (name.matches(NAME_REGEX));
    }
}
