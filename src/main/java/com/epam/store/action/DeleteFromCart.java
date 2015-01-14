package com.epam.store.action;

import com.epam.store.model.Cart;
import com.epam.store.servlet.Context;
import com.epam.store.servlet.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteFromCart implements Action {
    private static final Logger log = LoggerFactory.getLogger(DeleteFromCart.class);

    @Override
    public ActionResult execute(Context context) {
        Cart cart = (Cart) context.getAttribute("cart", Scope.SESSION);
        String[] productToDelete = context.getParameterValues("productIdToDelete");
        for (String productID : productToDelete) {
            log.debug("Selected product ID to delete: " + productID);
            Long id = Long.valueOf(productID);
            cart.removeProduct(id);
        }
        return new ActionResult(context.getPreviousURI(), true);
    }
}
