package com.epam.store.action;

import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

public abstract class AbstractCreatingProductAction implements Action {

    @Override
    public abstract ActionResult execute(WebContext webContext);

    /**
     * Gets parameters and sets they to flash scope
     * for displaying on the same page if error occurred
     */
    protected void setAttributesToFlashScope(WebContext webContext) {
        webContext.setAttribute("attributesAmount", webContext.getParameter("attributesAmount"), Scope.FLASH);
        webContext.setAttribute("categoryName", webContext.getParameter("categoryName"), Scope.FLASH);
        webContext.setAttribute("productName", webContext.getParameter("productName"), Scope.FLASH);
        webContext.setAttribute("price", webContext.getParameter("price"), Scope.FLASH);
        webContext.setAttribute("description", webContext.getParameter("description"), Scope.FLASH);
        webContext.setAttribute("attributeNames", webContext.getParameterValues("attributeNames"), Scope.FLASH);
        webContext.setAttribute("attributeValues", webContext.getParameterValues("attributeValues"), Scope.FLASH);
    }
}
