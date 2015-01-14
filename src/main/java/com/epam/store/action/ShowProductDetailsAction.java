package com.epam.store.action;

import com.epam.store.model.Product;
import com.epam.store.service.ProductService;
import com.epam.store.servlet.Context;
import com.epam.store.servlet.Scope;

public class ShowProductDetailsAction implements Action {
    private ActionResult actionResult = new ActionResult("details");

    @Override
    public ActionResult execute(Context context) {
        String id = context.getParameter("id");
        if (id != null) {
            ProductService productService = context.getService(ProductService.class);
            Product product = productService.getProductByID(Long.valueOf(id));
            context.setAttribute("product", product, Scope.REQUEST);
            context.setAttribute("attributes", product.getAttributes(), Scope.REQUEST);
        }
        return actionResult;
    }
}
