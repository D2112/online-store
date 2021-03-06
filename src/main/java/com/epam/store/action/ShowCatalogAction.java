package com.epam.store.action;

import com.epam.store.model.Product;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;

@WebAction(path = "GET/catalog")
public class ShowCatalogAction extends AbstractShowProductsAction {
    private ActionResult catalogPage = new ActionResult("catalog");

    @Override
    public ActionResult execute(WebContext webContext) {
        String categoryName = super.getCategoryNameFromPath(webContext);
        if (categoryName != null) {
            List<Product> products = super.getProducts(webContext, categoryName);
            webContext.setAttribute("products", products, Scope.REQUEST);
        }
        return catalogPage;
    }
}
