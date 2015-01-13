package com.epam.store.action;


import com.epam.store.servlet.Context;
import com.epam.store.servlet.Scope;
import com.epam.store.model.User;
import com.epam.store.service.Authenticator;

class LoginAction implements Action {
    private ActionResult errorResult = new ActionResult("login", true);
    private ActionResult successfulResult = new ActionResult("catalog", true);

    @Override
    public ActionResult execute(Context context) {;
        String email = context.getParameter("email");
        String password = context.getParameter("password");
        Authenticator authenticator = context.getService(Authenticator.class);
        User authenticatedUser = authenticator.authenticate(email, password);
        if(authenticatedUser == null) {
            context.setAttribute("loginResult", "Error: login or password is wrong", Scope.FLASH);
            return errorResult;
        }
        context.setAttribute("user", authenticatedUser, Scope.SESSION);
        return successfulResult;
    }
}
