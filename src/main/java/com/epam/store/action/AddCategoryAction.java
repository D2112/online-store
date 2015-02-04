package com.epam.store.action;

import com.epam.store.model.Category;
import com.epam.store.service.CategoryService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;
import java.util.ResourceBundle;

@WebAction(path = "POST/admin/categories/add")
public class AddCategoryAction implements Action {

    @Override
    @SuppressWarnings("unchecked")
    public ActionResult execute(WebContext webContext) {
        ResourceBundle messagesBundle = webContext.getMessagesBundle();
        String emptyCategoryMessage = messagesBundle.getString("adding-category.message.emptyCategory");
        String successMessage = messagesBundle.getString("adding-category.message.success");
        String categoryExistMessage = messagesBundle.getString("adding-category.message.exist");

        ActionResult backToPreviousPage = new ActionResult(webContext.getPreviousURI(), true);
        String categoryName = webContext.getParameter("categoryName");
        CategoryService service = webContext.getService(CategoryService.class);
        if (categoryName == null || categoryName.isEmpty()) {
            webContext.setAttribute("errorMessage", emptyCategoryMessage, Scope.FLASH);
            return backToPreviousPage;
        }
        Category addedCategory = service.addCategory(categoryName);
        //adding new category to category list in application context
        if (addedCategory != null) {
            List<Category> categories = (List<Category>) webContext.getAttribute("categories", Scope.APPLICATION);
            categories.add(addedCategory);
            webContext.setAttribute("successMessage", successMessage, Scope.FLASH);
        } else {
            webContext.setAttribute("errorMessage", categoryExistMessage, Scope.FLASH);
        }
        return backToPreviousPage;
    }
}
