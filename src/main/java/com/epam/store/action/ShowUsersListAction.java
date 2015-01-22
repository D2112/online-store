package com.epam.store.action;

import com.epam.store.service.UserService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

public class ShowUsersListAction implements Action {
    private ActionResult adminPage = new ActionResult("admin");

    @Override
    public ActionResult execute(WebContext webContext) {
        UserService userService = webContext.getService(UserService.class);
        webContext.setAttribute("users", userService.getAllUsers(), Scope.REQUEST);
        return adminPage;
    }
}
