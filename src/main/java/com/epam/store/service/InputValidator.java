package com.epam.store.service;

public class InputValidator {
    private static final int NAME_MAX_SIZE = 16;
    private static final String NAME_REGEX = "[A-Za-z0-9_]+";
    private static final String EMAIL_REGEX = "[A-Za-z0-9_]+@{1}+[A-Za-z]+.{1}+[A-Za-z]+";

    public boolean isEmailValid(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public boolean isNameValid(String name) {
        return (name.length() < NAME_MAX_SIZE) && (name.matches(NAME_REGEX));
    }
}
