package com.epam.store.action;

import com.epam.store.service.InputValidator;
import com.epam.store.service.RegistrationService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistrationAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(RegistrationAction.class);
    private ActionResult errorResult = new ActionResult("registration", true);
    private ActionResult successResult = new ActionResult("register-success", true);

    @Override
    public ActionResult execute(WebContext webContext) {
        String name = webContext.getParameter("name");
        String email = webContext.getParameter("email");
        String password = webContext.getParameter("password");
        String passwordConfirm = webContext.getParameter("passwordConfirm");
        RegistrationService registrationService = webContext.getService(RegistrationService.class);

        webContext.setAttribute("name", name, Scope.FLASH);
        webContext.setAttribute("email", email, Scope.FLASH);
        String errorMessage = checkValidationErrors(name, email, password, passwordConfirm);
        if (errorMessage != null) {
            webContext.setAttribute("error", errorMessage, Scope.FLASH);
            log.debug("Registration error: " + errorMessage);
            return errorResult;
        }
        if (!registrationService.register(name, email, password)) {
            errorMessage = email + " is already registered";
            webContext.setAttribute("error", errorMessage, Scope.FLASH);
            log.debug("Registration error: " + errorMessage);
            return errorResult;
        }
        return successResult;
    }

    private String checkValidationErrors(String name, String email, String password, String passwordConfirm) {
        if (!InputValidator.isEmailValid(email)) {
            return "Error: incorrect email";
        }
        if (!InputValidator.isNameValid(name)) {
            return "Error: incorrect name";
        }
        if(!password.equals(passwordConfirm)) {
            return "Error: passwords are not equal";
        }
        return null;
    }
}

