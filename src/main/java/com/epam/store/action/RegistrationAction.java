package com.epam.store.action;

import com.epam.store.controller.Scope;
import com.epam.store.controller.Context;
import com.epam.store.service.RegistrationService;

public class RegistrationAction implements Action {
    private ActionResult registrationResult = new ActionResult("registration", true);

    @Override
    public ActionResult execute(Context context) {
        String name = context.getParameter("name");
        String email = context.getParameter("email");
        String password = context.getParameter("password");
        RegistrationService registrationService = context.getService(RegistrationService.class);
        //if registration is successful
        if (registrationService.register(name, email, password)) {
            context.setAttribute("registerResult", email + " successfully registered", Scope.SESSION);
        } else {
            context.setAttribute("name", name, Scope.SESSION);
            context.setAttribute("email", email, Scope.SESSION);
            context.setAttribute("registerResult", "Error: " + email + " is already registered", Scope.SESSION);
        }
        return registrationResult;
    }
}
