package com.epam.store.controller.action;

import com.epam.store.service.RegistrationService;

public class RegistrationAction implements Action {
    private ActionResult home = new ActionResult("home", true);
    private ActionResult error = new ActionResult("registration");

    @Override
    public ActionResult execute(Context context) {
        String name = context.getParameter("name");
        String email = context.getParameter("email");
        String password = context.getParameter("password");
        RegistrationService registrationService = null;
        if (registrationService.register(name, email, password)) {
            return home;
        }
        context.setAttributeToRequest("error", email + " is already registered");
        return error;
    }
}
