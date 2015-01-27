package com.epam.store.servlet;

import com.epam.store.action.Action;
import com.epam.store.action.ActionFactory;
import com.epam.store.action.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "Controller", urlPatterns = "/controller/*")
@MultipartConfig
public class ControllerServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ControllerServlet.class);
    private ActionFactory actionFactory;
    private WebContext webContext;

    @Override
    public void init() throws ServletException {
        actionFactory = ActionFactory.getInstance();
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        webContext = new WebContext(req, resp);
        log.debug("Requested action: " + webContext.getRequestedAction());
        log.debug("current URI: " + webContext.getURI());
        log.debug("Referrer: " + req.getHeader("Referrer"));
        Action action = actionFactory.getAction(webContext);
        if (action == null) {
            log.debug("Action not found");
            webContext.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        log.debug("Found action: " + action.getClass().getSimpleName());
        ActionResult result = action.execute(webContext);
        doForwardOrRedirect(result, req, resp);
    }

    private void doForwardOrRedirect(ActionResult result, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (result.isRedirect()) {
            String location = webContext.getContextPath() + "/" + result.getPageName();
            log.debug("Redirect requested location: " + location);
            webContext.sendRedirect(location);
        } else {
            String path = "/WEB-INF/jsp/" + result.getPageName() + ".jsp";
            log.debug("Forward requested path: " + path);
            webContext.forward(path);
        }
    }
}
