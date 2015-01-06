package com.epam.store.controller;

import com.epam.store.controller.action.Action;
import com.epam.store.controller.action.ActionFactory;
import com.epam.store.controller.action.ActionResult;
import com.epam.store.controller.action.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ControllerServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ControllerServlet.class);
    private ActionFactory actionFactory;

    @Override
    public void init() throws ServletException {
        actionFactory = ActionFactory.getInstance();
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context(req, resp);
        log.info("Requested page: " + context.getRequestedPage());
        Action action = actionFactory.getAction(context);
        if (action == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
            return;
        }
        ActionResult result = action.execute(context);
        doForwardOrRedirect(result, req, resp);
    }

    private void doForwardOrRedirect(ActionResult result, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (result.isRedirect()) {
            String location = req.getContextPath() + "/do/" + result.getPageName();
            resp.sendRedirect(location);
        } else {
            String path = String.format("/WEB-INF/jsp/" + result.getPageName() + ".jsp");
            req.getRequestDispatcher(path).forward(req, resp);
        }
    }
}
