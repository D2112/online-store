package com.epam.store.action;

import com.epam.store.model.*;
import com.epam.store.service.PurchaseService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@WebAction(path = "POST/user/confirmOrder")
public class ConfirmOrderAction implements Action {
    private static final String DEFAULT_STATUS = Status.DELIVERY;
    private ActionResult toCartPage = new ActionResult("cart", true);

    @Override
    public ActionResult execute(WebContext webContext) {
        ResourceBundle messagesBundle = webContext.getMessagesBundle();
        Cart cart = (Cart) webContext.getAttribute("cart", Scope.SESSION);
        User user = (User) webContext.getAttribute("user", Scope.SESSION);
        List<Product> products = cart.getProducts();
        List<Purchase> purchaseList = new ArrayList<>();
        Date currentDate = new Date(System.currentTimeMillis());
        for (Product product : products) {
            purchaseList.add(new Purchase(product, product.getPrice(), currentDate, new Status(DEFAULT_STATUS)));
        }
        PurchaseService purchaseService = webContext.getService(PurchaseService.class);
        purchaseService.addPurchaseListToUser(user.getId(), purchaseList);
        cart.removeAllProducts();
        webContext.setAttribute("successMessage", messagesBundle.getString("cart.message.success"), Scope.FLASH);
        return toCartPage;
    }
}
