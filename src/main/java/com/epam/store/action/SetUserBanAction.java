package com.epam.store.action;

import com.epam.store.service.UserService;
import com.epam.store.servlet.WebContext;

@WebAction(path = "POST/admin/setUserBan")
public class SetUserBanAction implements Action {

    @Override
    public ActionResult execute(WebContext webContext) {
        String userIdParameter = webContext.getParameter("id");
        String banStringValue = webContext.getParameter("banValue");
        boolean banValue = Boolean.valueOf(banStringValue);
        if (userIdParameter != null && !userIdParameter.isEmpty()) {
            long userID = Long.valueOf(userIdParameter);
            UserService userService = webContext.getService(UserService.class);
            userService.setUserBan(userID, banValue);
        }
        return new ActionResult(webContext.getPreviousURI(), true);
    }
}
