package com.epam.store.action;

import com.epam.store.listener.SessionListener;
import com.epam.store.model.Cart;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebAction(path = "POST/deleteFromCart")
public class DeleteFromCart implements Action {
    private static final Logger log = LoggerFactory.getLogger(DeleteFromCart.class);

    @Override
    public ActionResult execute(WebContext webContext) {
        Cart cart = (Cart) webContext.getAttribute(SessionListener.CART_ATTRIBUTE_NAME, Scope.SESSION);
        String[] productToDelete = webContext.getParameterValues("productIdToDelete");
        if (productToDelete != null) {
            for (String productID : productToDelete) {
                log.debug("Selected product ID to delete: " + productID);
                Long id = Long.valueOf(productID);
                cart.removeProduct(id);
            }
        }
        return new ActionResult(webContext.getPreviousURI(), true);
    }
}
