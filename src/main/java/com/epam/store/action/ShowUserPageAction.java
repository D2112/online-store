package com.epam.store.action;

import com.epam.store.model.Order;
import com.epam.store.model.User;
import com.epam.store.service.PurchaseService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;

@WebAction(path = "GET/user")
public class ShowUserPageAction implements Action {
    private ActionResult userPage = new ActionResult("user");

    @Override
    public ActionResult execute(WebContext webContext) {
        User user = (User) webContext.getAttribute("user", Scope.SESSION);
        PurchaseService purchaseService = webContext.getService(PurchaseService.class);
        List<Order> userOrderList = purchaseService.getUserOrderList(user.getId());
        webContext.setAttribute("orderList", userOrderList, Scope.REQUEST);
        return userPage;
    }
}
