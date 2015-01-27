package com.epam.store.action;

import com.epam.store.servlet.WebContext;

@WebAction(path = "POST/admin/creating-product/setAttributesAmount")
public class SetAttributesAmountAction extends AbstractCreatingProductAction {
    ActionResult result = new ActionResult("admin/creating-product", true);

    @Override
    public ActionResult execute(WebContext webContext) {
        super.setAttributesToFlashScope(webContext);
        return result;
    }
}
