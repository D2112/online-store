package com.epam.store.config;

public class ApplicationSettings {
    private static final PageConfig pageConfig;

    static {
        ConfigParser configParser = new ConfigParser();
        pageConfig = configParser.readPageConfig();
    }

    public static PageConfig getPageConfig() {
        return pageConfig;
    }
}
