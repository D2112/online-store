package com.epam.store.action;


import com.epam.store.model.User;
import com.epam.store.service.Authenticator;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

@WebAction(path = "POST/login")
class LoginAction implements Action {
    private ActionResult errorResult = new ActionResult("login", true);
    private ActionResult successfulResult = new ActionResult("catalog", true);

    @Override
    public ActionResult execute(WebContext webContext) {
        ;
        String email = webContext.getParameter("email");
        String password = webContext.getParameter("password");
        Authenticator authenticator = webContext.getService(Authenticator.class);
        User authenticatedUser = authenticator.authenticate(email, password);
        if (authenticatedUser == null) {
            webContext.setAttribute("email", email, Scope.FLASH);
            webContext.setAttribute("loginResult", "Error: login or password is wrong", Scope.FLASH);
            return errorResult;
        }
        webContext.setAttribute("user", authenticatedUser, Scope.SESSION);
        return successfulResult;
    }
}
