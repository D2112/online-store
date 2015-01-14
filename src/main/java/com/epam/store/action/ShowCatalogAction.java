package com.epam.store.action;

import com.epam.store.servlet.Context;
import com.epam.store.servlet.Scope;
import com.epam.store.model.Product;
import com.epam.store.service.ProductService;

import java.util.List;

public class ShowCatalogAction implements Action {
    private ActionResult actionResult = new ActionResult("catalog");

    @Override
    public ActionResult execute(Context context) {
        ProductService productService = context.getService(ProductService.class);
        String category = context.getParameter("category");
        if(category != null) {
            List<Product> products = productService.getProductsForCategory(category);
            context.setAttribute("products", products, Scope.REQUEST);
        }
        return actionResult;
    }
}
