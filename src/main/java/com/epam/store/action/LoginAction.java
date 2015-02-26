package com.epam.store.action;


import com.epam.store.model.User;
import com.epam.store.service.UserService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.ResourceBundle;

@WebAction(path = "POST/login")
public class LoginAction implements Action {
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";
    private ActionResult errorResult = new ActionResult("login", true);
    private ActionResult successfulResult = new ActionResult("catalog", true);

    @Override
    public ActionResult execute(WebContext webContext) {
        ResourceBundle messagesBundle = webContext.getMessagesBundle();
        String email = webContext.getParameter(USER_EMAIL);
        String password = webContext.getParameter(USER_PASSWORD);
        UserService userService = webContext.getService(UserService.class);
        User authenticatedUser = userService.authenticateUser(email, password);
        if (email == null || password == null) {
            webContext.setAttribute(USER_EMAIL, email, Scope.FLASH);
            webContext.setAttribute("errorMessage", messagesBundle.getString("login.error.emptyFields"), Scope.FLASH);
            return errorResult;
        }
        if (authenticatedUser == null) {
            webContext.setAttribute(USER_EMAIL, email, Scope.FLASH);
            webContext.setAttribute("errorMessage", messagesBundle.getString("login.error.notFound"), Scope.FLASH);
            return errorResult;
        }
        if (authenticatedUser.getBanned()) {
            webContext.setAttribute("errorMessage", messagesBundle.getString("login.error.ban"), Scope.FLASH);
            return errorResult;
        }
        webContext.setAttribute("user", authenticatedUser, Scope.SESSION);
        return successfulResult;
    }
}
