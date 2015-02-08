package com.epam.store.action;


import com.epam.store.model.User;
import com.epam.store.service.UserService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.ResourceBundle;

@WebAction(path = "POST/login")
public class LoginAction implements Action {
    private ActionResult errorResult = new ActionResult("login", true);
    private ActionResult successfulResult = new ActionResult("catalog", true);

    @Override
    public ActionResult execute(WebContext webContext) {
        ResourceBundle messagesBundle = webContext.getMessagesBundle();
        String email = webContext.getParameter("email");
        String password = webContext.getParameter("password");
        UserService userService = webContext.getService(UserService.class);
        User authenticatedUser = userService.authenticate(email, password);
        if (authenticatedUser == null) {
            webContext.setAttribute("email", email, Scope.FLASH);
            webContext.setAttribute("loginResult", messagesBundle.getString("login.error.notFound"), Scope.FLASH);
            return errorResult;
        }
        if (authenticatedUser.getBanned()) {
            webContext.setAttribute("loginResult", messagesBundle.getString("login.error.ban"), Scope.FLASH);
            return errorResult;
        }
        webContext.setAttribute("user", authenticatedUser, Scope.SESSION);
        return successfulResult;
    }
}
