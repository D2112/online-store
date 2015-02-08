package com.epam.store.action;

import com.epam.store.service.UserService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;


@WebAction(path = "POST/registration")
public class RegistrationAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(RegistrationAction.class);
    private ActionResult errorResult = new ActionResult("registration", true);
    private ActionResult successResult = new ActionResult("register-success", true);
    private ResourceBundle messagesBundle;

    @Override
    public ActionResult execute(WebContext webContext) {
        messagesBundle = webContext.getMessagesBundle();
        String name = webContext.getParameter("name");
        String email = webContext.getParameter("email");
        String password = webContext.getParameter("password");
        String passwordConfirm = webContext.getParameter("passwordConfirm");
        UserService userService = webContext.getService(UserService.class);
        //set attributes to flash scope for displaying after redirect if error
        webContext.setAttribute("name", name, Scope.FLASH);
        webContext.setAttribute("email", email, Scope.FLASH);
        String errorMessage = checkValidationErrors(name, email, password, passwordConfirm);
        if (errorMessage != null) {
            webContext.setAttribute("error", errorMessage, Scope.FLASH);
            log.debug("Registration error: " + errorMessage);
            return errorResult;
        }
        if (userService.findUser(email) != null) {
            errorMessage = email + messagesBundle.getString("registration.error.registered");
            webContext.setAttribute("error", errorMessage, Scope.FLASH);
            log.debug("Registration error: " + errorMessage);
            return errorResult;
        }
        userService.registerUser(name, email, password);
        return successResult;
    }

    private String checkValidationErrors(String name, String email, String password, String passwordConfirm) {
        if (!RegexValidator.isEmailValid(email)) {
            return messagesBundle.getString("registration.error.email");
        }
        if (!RegexValidator.isNameValid(name)) {
            return messagesBundle.getString("registration.error.name");
        }
        if(!password.equals(passwordConfirm)) {
            return messagesBundle.getString("registration.error.password");
        }
        return null;
    }
}

