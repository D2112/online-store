package com.epam.store.action;

import com.epam.store.servlet.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShowCreatingProductPageAction extends AbstractCreatingProductAction {
    private static final Logger log = LoggerFactory.getLogger(ShowCreatingProductPageAction.class);
    private static final int ATTRIBUTE_INPUT_FORMS_AMOUNT_DEFAULT = 1;
    ActionResult result;

    public ShowCreatingProductPageAction(boolean redirect) {
        if(redirect) result = new ActionResult("admin/creating-product", true);
        else result = new ActionResult("creating-product");
    }

    @Override
    public ActionResult execute(WebContext webContext) {
        String attributesAmountString = webContext.getParameter("attributesAmount");
        Integer attributesAmount;
        if(attributesAmountString == null || attributesAmountString.isEmpty()) {
            attributesAmount = ATTRIBUTE_INPUT_FORMS_AMOUNT_DEFAULT;
        } else {
            attributesAmount = Integer.valueOf(attributesAmountString);
        }
        super.setAttributesToFlashScope(webContext);
        log.debug("attribute count: " + attributesAmount);
        log.debug("selected category: " + webContext.getParameter("category"));
        return result;
    }
}
