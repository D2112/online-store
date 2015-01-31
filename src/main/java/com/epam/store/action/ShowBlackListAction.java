package com.epam.store.action;

import com.epam.store.model.User;
import com.epam.store.service.UserService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;
import java.util.stream.Collectors;


@WebAction(path = "GET/admin/black-list")
public class ShowBlackListAction implements Action {
    private ActionResult adminPage = new ActionResult("admin");

    @Override
    public ActionResult execute(WebContext webContext) {
        UserService userService = webContext.getService(UserService.class);
        List<User> allUsers = userService.getAllUsers();
        List<User> bannedUsers = allUsers.stream().filter(User::getBanned).collect(Collectors.toList());
        webContext.setAttribute("blackList", bannedUsers, Scope.REQUEST);
        return adminPage;
    }
}
