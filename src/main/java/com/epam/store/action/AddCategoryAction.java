package com.epam.store.action;

import com.epam.store.listener.ContextListener;
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
        ActionResult backToPreviousPage = new ActionResult(webContext.getPreviousURI(), true);
        String categoryName = webContext.getParameter("categoryName");
        CategoryService service = webContext.getService(CategoryService.class);
        if (categoryName == null || categoryName.isEmpty()) {
            webContext.setAttribute
                    ("errorMessage", messagesBundle.getString("adding-category.message.emptyCategory"), Scope.FLASH);
            return backToPreviousPage;
        }
        Category addedCategory = service.addCategory(categoryName);
        //adding new category to category list in application context
        if (addedCategory != null) {
            List<Category> categories = (List<Category>)
                    webContext.getAttribute(ContextListener.CATEGORY_LIST_ATTRIBUTE_NAME, Scope.APPLICATION);
            categories.add(addedCategory);
            webContext.setAttribute
                    ("successMessage", messagesBundle.getString("adding-category.message.success"), Scope.FLASH);
        } else {
            webContext.setAttribute
                    ("errorMessage", messagesBundle.getString("adding-category.message.exist"), Scope.FLASH);
        }
        return backToPreviousPage;
    }
}
