package com.epam.store.action;

import com.epam.store.servlet.Scope;
import com.epam.store.servlet.Context;
import com.epam.store.service.InputValidator;
import com.epam.store.service.RegistrationService;

public class RegistrationAction implements Action {
    private ActionResult registrationResult = new ActionResult("registration", true);

    @Override
    public ActionResult execute(Context context) {
        String name = context.getParameter("name");
        String email = context.getParameter("email");
        String password = context.getParameter("password");
        RegistrationService registrationService = context.getService(RegistrationService.class);
        String resultMessage = register(name, email, password, registrationService);
        context.setAttribute("name", name, Scope.FLASH);
        context.setAttribute("email", email, Scope.FLASH);
        context.setAttribute("registerResult", resultMessage, Scope.FLASH);
        return registrationResult;
    }

    private String register(String name, String email, String password, RegistrationService registrationService) {
        InputValidator inputValidator = new InputValidator();
        if (!inputValidator.isEmailValid(email)) {
            return "Error: incorrect email";
        }
        if(!inputValidator.isNameValid(name)) {
            return "Error: incorrect name";
        }
        if (!registrationService.register(name, email, password)) {
            return "Error: " + email + " is already registered";
        }
        return (email + " successfully registered");
    }
}

