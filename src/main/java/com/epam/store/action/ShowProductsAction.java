package com.epam.store.action;

import com.epam.store.controller.Context;
import com.epam.store.controller.Scope;
import com.epam.store.model.Product;
import com.epam.store.service.ProductService;

import java.util.List;

public class ShowProductsAction implements Action {
    private ActionResult actionResult = new ActionResult("catalog");

    @Override
    public ActionResult execute(Context context) {
        ProductService service = context.getService(ProductService.class);
        String category = context.getParameter("category");
        if(category != null) {
            List<Product> products = service.getProductsForCategory(category);
            context.setAttribute("products", products, Scope.REQUEST);
        }
        return actionResult;
    }
}
