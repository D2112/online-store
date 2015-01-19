package com.epam.store.action;

import com.epam.store.service.ProductService;
import com.epam.store.service.UserService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;

public class ShowAdminPageAction implements Action {
    private static final String USERS_TABLE_PAGE = "users";
    private static final String PRODUCTS_TABLE_PAGE = "products";
    private ActionResult adminPage = new ActionResult("admin");

    @Override
    public ActionResult execute(WebContext webContext) {
        List<String> parametersFromPath = webContext.getParametersFromPath();
        if (parametersFromPath.size() == 0) {
            return adminPage;
        }
        String pathParameter = parametersFromPath.iterator().next().toLowerCase();
        if(pathParameter.equals("users")) {
            UserService userService = webContext.getService(UserService.class);
            webContext.setAttribute("users", userService.getAllUsers(), Scope.REQUEST);
            return adminPage;
        }
        if(pathParameter.equals("products")) {
            ProductService productService = webContext.getService(ProductService.class);
            //webContext.setAttribute("products", productService., Scope.REQUEST);
            return adminPage;
        }
        return new ActionResult("admin", true);
    }
}
