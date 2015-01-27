package com.epam.store.action;

import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

@WebAction(path = "GET/logout")
public class LogoutAction implements Action {
    private ActionResult backToStartPage = new ActionResult("catalog", true);

    @Override
    public ActionResult execute(WebContext webContext) {
        webContext.removeAttribute("user", Scope.SESSION);
        return backToStartPage;
    }
}
