package com.epam.store.action;

import com.epam.store.servlet.Context;
import com.epam.store.servlet.Scope;

public class LogoutAction implements Action {
    private ActionResult backToStartPage = new ActionResult("catalog", true);

    @Override
    public ActionResult execute(Context context) {
        context.removeAttribute("user", Scope.SESSION);
        return backToStartPage;
    }
}
