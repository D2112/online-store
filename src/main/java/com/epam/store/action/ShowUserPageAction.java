package com.epam.store.action;

import com.epam.store.model.Order;
import com.epam.store.model.User;
import com.epam.store.service.UserService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;

@WebAction(path = "GET/user")
public class ShowUserPageAction implements Action {
    private ActionResult userPage = new ActionResult("user");

    @Override
    public ActionResult execute(WebContext webContext) {
        User user = (User) webContext.getAttribute("user", Scope.SESSION);
        UserService userService = webContext.getService(UserService.class);
        List<Order> userOrderList = userService.getUserOrderList(user.getId());
        webContext.setAttribute("orderList", userOrderList, Scope.REQUEST);
        return userPage;
    }
}
