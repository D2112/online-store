package com.epam.store.config;

import java.util.List;
import java.util.Map;

public class PageConfig {
    private Map<String, String> UriByJspPageNameMap;
    private List<String> pagesWithUriParameters;
    private String startPage;

    public PageConfig() {
    }

    public PageConfig(Map<String, String> UriByJspPageNameMap, List<String> pagesWithUriParameters, String startPage) {
        this.UriByJspPageNameMap = UriByJspPageNameMap;
        this.pagesWithUriParameters = pagesWithUriParameters;
        this.startPage = startPage;
    }

    public Map<String, String> getUriByJspPageNameMap() {
        return UriByJspPageNameMap;
    }

    public List<String> getPagesWithUriParameters() {
        return pagesWithUriParameters;
    }

    public String getStartPage() {
        return startPage;
    }
}
