package com.epam.store.action;

import com.epam.store.model.Cart;
import com.epam.store.model.Product;
import com.epam.store.service.ProductService;
import com.epam.store.servlet.Context;
import com.epam.store.servlet.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddToCartAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(AddToCartAction.class);

    @Override
    public ActionResult execute(Context context) {
        Cart cart = (Cart) context.getAttribute("cart", Scope.SESSION);
        Long id = Long.valueOf(context.getParameter("id"));
        if (id != null) {
            ProductService service = context.getService(ProductService.class);
            log.debug("Product id for adding to cart: " + id);
            Product product = service.getProductByID(id);
            if (product != null) {
                cart.addProduct(product);
                log.debug("Added to cart: " + product.getName());
                log.debug("Cart size: " + cart.productAmount());
            }
        }
        return new ActionResult(context.getPreviousURI(), true);
    }
}
