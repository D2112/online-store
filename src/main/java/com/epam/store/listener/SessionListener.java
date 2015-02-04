package com.epam.store.listener;

import com.epam.store.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

@WebListener
public class SessionListener implements HttpSessionListener {
    private static final Logger log = LoggerFactory.getLogger(SessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        log.debug("creating session with id: " + httpSessionEvent.getSession().getId());
        httpSessionEvent.getSession().setAttribute("cart", new Cart());
        httpSessionEvent.getSession().setAttribute("locale", Locale.ENGLISH);
        //setting locale for jstl formatter
        Config.set(httpSessionEvent.getSession(), Config.FMT_LOCALE, Locale.ENGLISH);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        log.debug("destroying session with id: " + httpSessionEvent.getSession().getId());
    }
}
