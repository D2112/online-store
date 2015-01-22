package com.epam.store.action;

import com.epam.store.model.Product;
import com.epam.store.service.ProductService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowCatalogAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(ShowCatalogAction.class);
    private ActionResult actionResult = new ActionResult("catalog");

    @Override
    public ActionResult execute(WebContext webContext) {
        ProductService productService = webContext.getService(ProductService.class);
        List<String> parametersFromPath = webContext.getParametersFromURI(); //category's parameter passes as a path part
        if (parametersFromPath.size() > 0) {
            String category = parametersFromPath.iterator().next();
            log.debug("Showing category: " + category);
            List<Product> products = productService.getProductsForCategory(category);
            webContext.setAttribute("products", products, Scope.REQUEST);
        }
        return actionResult;
    }
}
