package com.epam.store.action;

import java.util.Map;

public class ActionResult {
    private String page;
    private boolean redirect;
    private Map<String, String[]> parameterMap;

    public ActionResult(String page) {
        this.page = page;
    }

    public ActionResult(String page, boolean redirect) {
        this.page = page;
        this.redirect = redirect;
    }

    public ActionResult(String page, boolean redirect, Map<String, String[]> parameterMap) {
        this.page = page;
        this.redirect = redirect;
        this.parameterMap = parameterMap;
    }

    public String getPageName() {
        return page;
    }

    public String getParamsQueryString() {
        if (parameterMap == null || parameterMap.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append('?');
        int counter = 0;
        for (Map.Entry<String, String[]> paramEntry : parameterMap.entrySet()) {
            counter++;
            sb.append(paramEntry.getKey());
            sb.append('=');
            String[] values = paramEntry.getValue();
            for (int i = 0; i < values.length; i++) {
                sb.append(values[i]);
                if (i != values.length - 1) sb.append('%');
            }
            if (counter != parameterMap.size() - 1) sb.append('&');
        }
        return sb.toString();
    }

    public boolean isRedirect() {
        return redirect;
    }
}
