package com.epam.store.action;

import com.epam.store.model.Category;
import com.epam.store.service.CategoryService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;

@WebAction(path = "POST/admin/categories/add")
public class AddCategoryAction implements Action {

    @Override
    @SuppressWarnings("unchecked")
    public ActionResult execute(WebContext webContext) {
        ActionResult backToPreviousPage = new ActionResult(webContext.getPreviousURI(), true);
        String categoryName = webContext.getParameter("categoryName");
        CategoryService service = webContext.getService(CategoryService.class);
        if (categoryName == null || categoryName.isEmpty()) {
            webContext.setAttribute("errorMessage", "Category field is empty", Scope.FLASH);
            return backToPreviousPage;
        }
        Category addedCategory = service.addCategory(categoryName);
        //adding new category to application context
        if (addedCategory != null) {
            List<Category> categories = (List<Category>) webContext.getAttribute("categories", Scope.APPLICATION);
            categories.add(addedCategory);
            webContext.setAttribute("successMessage", "Category added successfully", Scope.FLASH);
        } else {
            webContext.setAttribute("errorMessage", "Category already exist", Scope.FLASH);
        }
        return backToPreviousPage;
    }
}
