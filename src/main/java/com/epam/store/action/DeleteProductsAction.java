package com.epam.store.action;

import com.epam.store.service.ProductService;
import com.epam.store.servlet.WebContext;

@WebAction(path = "POST/deleteProducts")
public class DeleteProductsAction implements Action {
    @Override
    public ActionResult execute(WebContext webContext) {
        String[] productToDelete = webContext.getParameterValues("productIdToDelete");
        if (productToDelete != null) {
            ProductService productService = webContext.getService(ProductService.class);
            for (String productID : productToDelete) {
                Long id = Long.valueOf(productID);
                productService.deleteProduct(id);
            }
        }
        return new ActionResult(webContext.getPreviousURI(), true);
    }
}
