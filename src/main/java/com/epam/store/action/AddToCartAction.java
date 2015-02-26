package com.epam.store.action;

import com.epam.store.listener.SessionListener;
import com.epam.store.model.Cart;
import com.epam.store.model.Product;
import com.epam.store.service.ProductService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebAction(path = "POST/addToCart")
public class AddToCartAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(AddToCartAction.class);

    @Override
    public ActionResult execute(WebContext webContext) {
        Cart cart = (Cart) webContext.getAttribute(SessionListener.CART_ATTRIBUTE_NAME, Scope.SESSION);
        Long id = Long.valueOf(webContext.getParameter("id"));
        if (id != null) {
            ProductService service = webContext.getService(ProductService.class);
            log.debug("Product id for adding to cart: " + id);
            Product product = service.getProductByID(id);
            if (product != null) {
                cart.addProduct(product);
                log.debug("Added to cart: " + product.getName());
                log.debug("Cart size: " + cart.getProductAmount());
            }
        }
        return new ActionResult(webContext.getPreviousURI(), true);
    }
}
