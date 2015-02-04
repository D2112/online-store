package com.epam.store.action;

import com.epam.store.model.Product;
import com.epam.store.service.ProductService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@WebAction(path = "GET/catalog")
public class ShowCatalogAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(ShowCatalogAction.class);
    private ActionResult actionResult = new ActionResult("catalog");

    @Override
    public ActionResult execute(WebContext webContext) {
        ProductService productService = webContext.getService(ProductService.class);
        List<String> parametersFromPath = webContext.getParametersFromURI(); //category's parameter passes as a path part
        if (parametersFromPath.size() > 0) {
            String category = parametersFromPath.iterator().next();
            try {
                category = URLDecoder.decode(category, "UTF-8"); //decode category name from URL decoding
            } catch (UnsupportedEncodingException e) {
                throw new ActionException(e);
            }
            log.debug("Showing category: " + category);
            List<Product> products = productService.getProductsForCategory(category);
            webContext.setAttribute("products", products, Scope.REQUEST);
        }
        return actionResult;
    }
}
