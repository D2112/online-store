package com.epam.store.action;

import com.epam.store.model.Order;
import com.epam.store.model.User;
import com.epam.store.service.PurchaseService;
import com.epam.store.service.UserService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;

@WebAction(path = "GET/admin/purchase-list")
public class ShowOrderListAction implements Action {
    private ActionResult actionResult = new ActionResult("user-orders");

    @Override
    public ActionResult execute(WebContext webContext) {
        String userStringID = webContext.getFirstParameterFromPath();
        UserService userService = webContext.getService(UserService.class);
        PurchaseService purchaseService = webContext.getService(PurchaseService.class);
        if (userStringID != null && RegexValidator.isIntegerNumber(userStringID)) {
            Long userID = Long.valueOf(userStringID);
            User user = userService.findUser(userID);
            List<Order> userOrderList = purchaseService.getUserOrderList(userID);
            webContext.setAttribute("user", user, Scope.REQUEST);
            webContext.setAttribute("orderList", userOrderList, Scope.REQUEST);
        }
        return actionResult;
    }
}
