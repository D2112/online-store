package com.epam.store.action;

import com.epam.store.model.Product;
import com.epam.store.service.ProductService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

@WebAction(path = "GET/details")
public class ShowProductDetailsAction implements Action {
    private ActionResult actionResult = new ActionResult("details");

    @Override
    public ActionResult execute(WebContext webContext) {
        String id = webContext.getParameter("id");
        if (id != null) {
            ProductService productService = webContext.getService(ProductService.class);
            Product product = productService.getProductByID(Long.valueOf(id));
            webContext.setAttribute("product", product, Scope.REQUEST);
            webContext.setAttribute("attributes", product.getAttributes(), Scope.REQUEST);
        }
        return actionResult;
    }
}
