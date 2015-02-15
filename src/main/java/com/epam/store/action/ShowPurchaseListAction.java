package com.epam.store.action;

import com.epam.store.model.Order;
import com.epam.store.service.UserService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;

@WebAction(path = "GET/admin/purchase-list")
public class ShowPurchaseListAction implements Action {
    private ActionResult actionResult = new ActionResult("admin");

    @Override
    public ActionResult execute(WebContext webContext) {
        String userID = webContext.getFirstParameterFromPath();
        UserService userService = webContext.getService(UserService.class);
        if (userID != null && RegexValidator.isIntegerNumber(userID)) {
            List<Order> userOrderList = userService.getUserOrderList(Long.valueOf(userID));
            webContext.setAttribute("orderList", userOrderList, Scope.REQUEST);
        }
        return actionResult;
    }
}
