package com.epam.store.action;

import com.epam.store.listener.ContextListener;
import com.epam.store.model.Category;
import com.epam.store.service.CategoryService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.List;

@WebAction(path = "POST/admin/categories/delete")
public class DeleteCategoryAction implements Action {

    @Override
    @SuppressWarnings("unchecked")
    public ActionResult execute(WebContext webContext) {
        String categoryName = webContext.getParameter("categoryName");
        CategoryService service = webContext.getService(CategoryService.class);
        if (categoryName != null && !categoryName.isEmpty()) {
            Category category = service.getCategory(categoryName);
            if (category != null) {
                service.deleteCategory(category);
                //removing category from the application context
                List<Category> categories = (List<Category>)
                        webContext.getAttribute(ContextListener.CATEGORY_LIST_ATTRIBUTE_NAME, Scope.APPLICATION);
                categories.remove(category);
            }
        }
        return new ActionResult(webContext.getPreviousURI(), true);
    }
}
