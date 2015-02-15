package com.epam.store.servlet;

import com.epam.store.config.ConfigParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Facade for work with {@link HttpServletRequest} and
 * {@link HttpServletResponse} classes.
 *
 * @see javax.servlet.http.HttpServletRequest
 * @see javax.servlet.http.HttpServletResponse
 */
public class WebContext {
    private static final Logger log = LoggerFactory.getLogger(WebContext.class);
    private static final String FLASH_ATTRIBUTE_PREFIX = "flash.";
    private static final String MESSAGES_BUNDLE_BASENAME = "i18n.MessagesBundle";
    private static final String LANGUAGE_COOKIE_ATTRIBUTE_NAME = "lang";
    private static final String LOCALE_ATTRIBUTE_NAME = "locale";
    private static final String SPLIT_URL_PATH_REGEX = "[^/]+";
    private static final List<String> pagesWithURIParameters;
    private HttpServletRequest req;
    private HttpServletResponse resp;

    static {
        pagesWithURIParameters = ConfigParser.getInstance().getPageConfig().getPagesWithUriParameters();
    }

    public WebContext(HttpServletRequest req, HttpServletResponse resp) {
        this.req = req;
        this.resp = resp;
        disableCaching();

    }

    public WebContext(ServletRequest servletRequest, ServletResponse servletResponse) {
        this((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
    }

    /**
     * Gets request method + URI path
     * example: GET/category/stuff
     *
     * @return the key-name of requested action from request
     */
    public String getRequestedAction() {
        return req.getMethod() + getPagePathFromURN();
    }

    public String getParameter(String parameterName) {
        return req.getParameter(parameterName);
    }

    public String[] getParameterValues(String name) {
        return req.getParameterValues(name);
    }

    public Map<String, String[]> getParameterMap() {
        return req.getParameterMap();
    }

    public void setAttribute(String name, Object value, Scope scope) {
        switch (scope) {
            case REQUEST:
                req.setAttribute(name, value);
                break;
            case SESSION:
                req.getSession().setAttribute(name, value);
                break;
            case APPLICATION:
                req.getServletContext().setAttribute(name, value);
                break;
            case FLASH:
                req.getSession().setAttribute(addFlashPrefixToName(name), value);
                break;
        }
    }

    public Object getAttribute(String name, Scope scope) {
        Object attributeObject = null;
        switch (scope) {
            case REQUEST:
                attributeObject = req.getAttribute(name);
                break;
            case SESSION:
                if (isSessionExist()) {
                    attributeObject = req.getSession().getAttribute(name);
                }
                break;
            case APPLICATION:
                attributeObject = req.getServletContext().getAttribute(name);
                break;
            case FLASH:
                if (isSessionExist()) {
                    attributeObject = req.getSession().getAttribute(addFlashPrefixToName(name));
                    if (attributeObject == null) attributeObject = req.getAttribute(name);
                }
                break;
        }
        return attributeObject;
    }

    public void removeAttribute(String name, Scope scope) {
        switch (scope) {
            case REQUEST:
                req.removeAttribute(name);
                break;
            case SESSION:
                if (isSessionExist()) {
                    req.getSession().removeAttribute(name);
                }
                break;
            case APPLICATION:
                req.getServletContext().removeAttribute(name);
                break;
            case FLASH:
                if (isSessionExist()) {
                    req.getSession().removeAttribute(addFlashPrefixToName(name));
                }
                break;
        }
    }

    public List<String> getAttributeNames(Scope scope) {
        List<String> attributeNames = new ArrayList<>();
        switch (scope) {
            case REQUEST:
                attributeNames = getListFromEnumeration(req.getAttributeNames());
                break;
            case SESSION:
                if (isSessionExist()) {
                    attributeNames = getListFromEnumeration(req.getSession().getAttributeNames());
                }
                break;
            case APPLICATION:
                attributeNames = getListFromEnumeration(req.getServletContext().getAttributeNames());
                break;
            case FLASH:
                if (isSessionExist()) {
                    Enumeration<String> attributeEnumeration = req.getSession().getAttributeNames();
                    attributeNames = getAttributeNamesForFlashScope(attributeEnumeration);
                }
                break;
        }
        return attributeNames;
    }

    public void forward(String path) throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }

    public void sendRedirect(String location) throws IOException {
        resp.sendRedirect(location);
    }

    public void sendError(int error) throws IOException {
        resp.sendError(error);
    }

    public String getContextPath() {
        return req.getContextPath();
    }

    public String getPreviousURI() {
        return req.getHeader("Referer").substring(getURL().length());
    }

    /**
     * Constructs url from parts
     * Scheme + server name + port
     * @return string with url
     */
    public String getURL() {
        StringBuilder sb = new StringBuilder();
        sb.append(req.getScheme());
        sb.append("://");
        sb.append(req.getServerName());
        sb.append(":");
        sb.append(req.getServerPort());
        sb.append("/");
        return sb.toString();
    }

    public String getURI() {
        return req.getRequestURI();
    }

    /**
     * Gets path from uri, it's like {@link #getPathInfo} method
     * but the difference is what this method looks pages config and search there
     * pages with uri-parameters after which rest part of the path cut.
     * For example if the path is: 'page/example/path'
     * and if in config present page with name 'example'
     * then methods return only 'page/example' string
     * @return full uri path or cut uri path, depends on page config
     * @see javax.servlet.http.HttpServletRequest#getPathInfo
     */
    public String getPagePathFromURN() {
        List<String> pathSegments = splitIntoSegments(req.getPathInfo());
        StringBuilder sb = new StringBuilder();
        for (String segment : pathSegments) {
            sb.append("/");
            sb.append(segment);
            if (pagesWithURIParameters.contains(segment)) {
                return sb.toString();
            }
        }
        if (sb.length() == 0) sb.append("/");
        return sb.toString();
    }

    /**
     * Gets list of parameters from path if in the page config present
     * the page after which path cut into parameters
     * @return list of string parameters from path
     */
    public List<String> getParametersFromPath() {
        String pathInfo = req.getPathInfo();
        String parameterString = pathInfo.substring(getPagePathFromURN().length());
        return splitIntoSegments(parameterString);
    }

    public String getFirstParameterFromURI() {
        List<String> parametersFromURI = getParametersFromPath();
        if (parametersFromURI.size() == 0) return null;
        return parametersFromURI.iterator().next();
    }

    public String getPathInfo() {
        return req.getPathInfo();
    }

    public Part getPart(String fileName) throws IOException, ServletException {
        return req.getPart(fileName);
    }

    public ResourceBundle getMessagesBundle() {
        Locale currentLocale = (Locale) req.getSession().getAttribute("locale");
        return ResourceBundle.getBundle(MESSAGES_BUNDLE_BASENAME, currentLocale);
    }

    public void setJstlFormatterLocale(Locale locale) {
        Config.set(req.getSession(), Config.FMT_LOCALE, locale);
    }

    public void addCookie(Cookie cookie) {
        resp.addCookie(cookie);
    }

    /**
     * Adds cookie to response with specified locale
     * The cookie adds on maximum possible age via integer max value
     * @param locale the locale which be added in cookie
     */
    public void addLangCookie(Locale locale) {
        Cookie cookie = new Cookie(LANGUAGE_COOKIE_ATTRIBUTE_NAME, locale.getLanguage());
        cookie.setMaxAge(Integer.MAX_VALUE);
        addCookie(cookie);
    }

    public Cookie findCookie(String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : req.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public void setLocale(Locale locale) {
        setAttribute(LOCALE_ATTRIBUTE_NAME, locale, Scope.SESSION);
        setJstlFormatterLocale(locale);
    }

    public HttpSession getSession() {
        return req.getSession();
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass) {
        return (T) getAttribute(serviceClass.getSimpleName(), Scope.APPLICATION);
    }

    private List<String> splitIntoSegments(String uri) {
        List<String> names = new ArrayList<>();
        String regex = SPLIT_URL_PATH_REGEX;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(uri);
        while (matcher.find()) {
            String parameter = matcher.group();
            if (parameter.length() > 0) names.add(matcher.group());
        }
        return names;
    }

    private String addFlashPrefixToName(String attributeName) {
        return FLASH_ATTRIBUTE_PREFIX + attributeName;
    }

    private boolean isSessionExist() {
        return req.getSession(false) != null;
    }

    private <T> List<T> getListFromEnumeration(Enumeration<T> e) {
        List<T> list = new ArrayList<>();
        while (e.hasMoreElements()) {
            T element = e.nextElement();
            list.add(element);
        }
        return list;
    }

    /**
     * Remove all names without flash prefix from the list,
     * and then remove flash prefix of the remaining names
     *
     * @return List of flash attribute names without flash prefix
     */
    private List<String> getAttributeNamesForFlashScope(Enumeration<String> enumeration) {
        List<String> attributeNames = new CopyOnWriteArrayList<>(getListFromEnumeration(enumeration));
        for (String attributeName : attributeNames) {
            if (!attributeName.startsWith(FLASH_ATTRIBUTE_PREFIX)) {
                attributeNames.remove(attributeName);
            }
        }
        attributeNames.replaceAll(s -> s.substring(FLASH_ATTRIBUTE_PREFIX.length()));
        return attributeNames;
    }

    private void disableCaching() {
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        resp.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        resp.setDateHeader("Expires", 0); // Proxies.
    }
}

