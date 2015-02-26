package com.epam.store.action;

import com.epam.store.service.PurchaseService;
import com.epam.store.servlet.Scope;
import com.epam.store.servlet.WebContext;

import java.util.HashMap;
import java.util.Map;

@WebAction(path = "POST/admin/changePurchaseStatus")
public class ChangePurchaseStatusAction implements Action {

    @Override
    public ActionResult execute(WebContext webContext) {
        String[] purchaseIDs = webContext.getParameterValues("purchaseID");
        String[] purchaseStatuses = webContext.getParameterValues("purchaseStatus");
        String userIdString = webContext.getParameter("userID");
        String orderNumber = webContext.getParameter("orderNumber");
        if (purchaseIDs.length != purchaseStatuses.length) {
            throw new ActionException("The amount of purchases not correlates to statuses amount");
        }
        PurchaseService service = webContext.getService(PurchaseService.class);
        Map<Long, String> purchaseStatusByID = getPurchaseStatusByIdMap(purchaseIDs, purchaseStatuses);
        service.updatePurchases(Long.parseLong(userIdString), purchaseStatusByID);
        //set order number to request for displaying which particular order has been changed
        webContext.setAttribute(orderNumber, orderNumber, Scope.FLASH);
        return new ActionResult(webContext.getPreviousURI(), true);
    }


    private Map<Long, String> getPurchaseStatusByIdMap(String[] purchaseIDs, String[] purchaseStatuses) {
        Map<Long, String> statusByID = new HashMap<>();
        long purchaseID;
        for (int i = 0; i < purchaseIDs.length; i++) {
            purchaseID = Long.parseLong(purchaseIDs[i]);
            statusByID.put(purchaseID, purchaseStatuses[i]);
        }
        return statusByID;
    }
}
