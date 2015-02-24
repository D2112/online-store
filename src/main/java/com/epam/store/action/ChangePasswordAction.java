package com.epam.store.action;

import com.epam.store.PasswordEncryptor;
import com.epam.store.model.User;
import com.epam.store.service.UserService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@WebAction(path = "POST/user/changePassword")
public class ChangePasswordAction implements Action {
    private static final String OLD_PASSWORD_NAME = "oldPassword";
    private static final String NEW_PASSWORD_NAME = "newPassword";
    private static final String CONFIRM_PASSWORD_NAME = "confirmNewPassword";
    private ActionResult errorResult = new ActionResult("user/change-password", true);
    private ActionResult successResult = new ActionResult("user", true);
    private ResourceBundle messagesBundle;

    @Override
    public ActionResult execute(WebContext webContext) {
        messagesBundle = webContext.getMessagesBundle();
        String oldPassword = webContext.getParameter(OLD_PASSWORD_NAME);
        String newPassword = webContext.getParameter(NEW_PASSWORD_NAME);
        String confirmNewPassword = webContext.getParameter(CONFIRM_PASSWORD_NAME);
        User user = (User) webContext.getAttribute("user", Scope.SESSION);
        List<String> validationErrors = checkValidationErrors(oldPassword, user, newPassword, confirmNewPassword);
        if (validationErrors.size() > 0) {
            webContext.setAttribute("errors", validationErrors, Scope.FLASH);
            return errorResult;
        }
        UserService userService = webContext.getService(UserService.class);
        userService.changeUserPassword(user, newPassword);
        webContext.setAttribute("successMessage", "Password successfully changed", Scope.FLASH);
        return successResult;
    }

    private List<String> checkValidationErrors(String oldPassword, User user, String newPassword, String confirmNewPassword) {
        List<String> errors = new ArrayList<>();
        if (!PasswordEncryptor.comparePassword(oldPassword, user.getPassword())) {
            errors.add(messagesBundle.getString("change-password.error.wrongPassword"));
        }
        if (!newPassword.equals(confirmNewPassword)) {
            errors.add(messagesBundle.getString("change-password.error.wrongPassword"));
        }
        return errors;
    }
}
