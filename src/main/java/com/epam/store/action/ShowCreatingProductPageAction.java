package com.epam.store.action;

import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebAction(path = "GET/admin/creating-product")
public class ShowCreatingProductPageAction extends AbstractCreatingProductAction {
    private static final Logger log = LoggerFactory.getLogger(ShowCreatingProductPageAction.class);
    ActionResult result = new ActionResult("creating-product");

    @Override
    public ActionResult execute(WebContext webContext) {
        super.setAttributesToFlashScope(webContext);
        return result;
    }
}
