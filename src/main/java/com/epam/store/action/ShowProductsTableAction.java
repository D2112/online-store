package com.epam.store.action;

import com.epam.store.model.Product;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;

@WebAction(path = "GET/admin/products")
public class ShowProductsTableAction extends AbstractShowProductsAction {
    private ActionResult adminPage = new ActionResult("product-management");

    @Override
    public ActionResult execute(WebContext webContext) {
        String categoryName = webContext.getParameter("category");
        if (categoryName != null) {
            List<Product> products = super.getProducts(webContext, categoryName);
            webContext.setAttribute("products", products, Scope.REQUEST);
            webContext.setAttribute("category", categoryName, Scope.FLASH);
        }
        return adminPage;
    }
}
