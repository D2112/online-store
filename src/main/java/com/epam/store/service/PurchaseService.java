package com.epam.store.service;

import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.Date;
import com.epam.store.model.Order;
import com.epam.store.model.Purchase;
import com.epam.store.model.Status;

import java.util.*;
import java.util.stream.Collectors;

public class PurchaseService {
    private static final String USER_ID_COLUMN = "USER_ID";
    private static final String DATE_TIME_COLUMN = "TIME";
    private static final String STATUS_NAME_COLUMN = "NAME";

    private DaoFactory daoFactory;

    public PurchaseService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Gets user's purchase list and forms with it order list.
     * Order is list of purchases committed at one time
     */
    public List<Order> getUserOrderList(long userID) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Purchase> purchaseDao = daoSession.getDao(Purchase.class);
            List<Purchase> purchaseList = purchaseDao.findByParameter(USER_ID_COLUMN, userID);
            return getOrderList(purchaseList);
        }
    }

    public void updatePurchases(long userID, Map<Long, String> purchaseStatusByID) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Purchase> purchaseDao = daoSession.getDao(Purchase.class);
            Dao<Status> statusDao = daoSession.getDao(Status.class);
            daoSession.beginTransaction();
            for (Map.Entry<Long, String> longStringEntry : purchaseStatusByID.entrySet()) {
                Long purchaseID = longStringEntry.getKey();
                String purchaseNewStatus = longStringEntry.getValue();
                Purchase purchase = purchaseDao.find(purchaseID);
                //compare status names
                if (purchase.getStatus().getName().equalsIgnoreCase(purchaseNewStatus)) {
                    continue; //no need to update
                }
                //check whether status name parameter correspond to constant names
                switch (purchaseNewStatus) {
                    case Status.CANCELED:
                    case Status.DELIVERY:
                    case Status.UNPAID:
                    case Status.PAID:
                        //try to find exist status with the same value and use them
                        Status status = statusDao.findFirstByParameter(STATUS_NAME_COLUMN, purchaseNewStatus);
                        purchase.setStatus(status);
                        purchaseDao.updateWithAdditionalParameter(purchase, USER_ID_COLUMN, userID);
                }
            }
            daoSession.endTransaction();
        }
    }

    public Purchase getPurchase(long purchaseID) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Purchase> purchaseDao = daoSession.getDao(Purchase.class);
            return purchaseDao.find(purchaseID);
        }
    }

    public void addPurchaseListToUser(Long userID, List<Purchase> purchaseList) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            daoSession.beginTransaction();
            for (Purchase purchase : purchaseList) {
                Dao<Purchase> purchaseDao = daoSession.getDao(Purchase.class);
                Dao<Date> dateDao = daoSession.getDao(Date.class);
                Dao<Status> statusDao = daoSession.getDao(Status.class);
                //try to find exist date and status with the same value and use them instead inserting
                Date existDate = dateDao.findFirstByParameter(DATE_TIME_COLUMN, purchase.getDate().getTime());
                Status existStatus = statusDao.findFirstByParameter(STATUS_NAME_COLUMN, purchase.getStatus().getName());
                //setting to purchase id of already exist date and time records
                if (existDate != null) purchase.getDate().setId(existDate.getId());
                if (existStatus != null) purchase.getStatus().setId(existStatus.getId());
                purchaseDao.insertWithAdditionalParameter(purchase, USER_ID_COLUMN, userID);
            }
            daoSession.endTransaction();
        }
    }

    private List<Order> getOrderList(List<Purchase> purchaseList) {
        List<Order> orderList = new ArrayList<>();
        Set<Date> dates = getUniqueDatesFromPurchaseList(purchaseList);
        for (Date date : dates) {
            List<Purchase> purchaseListWithTheSameDate = purchaseList.stream()
                    .filter(purchase -> purchase.getDate().equals(date)) //get all purchases with the same date
                    .collect(Collectors.toList());
            sortPurchaseListByStatus(purchaseListWithTheSameDate);
            orderList.add(new Order(purchaseListWithTheSameDate, date)); //form with them order
        }
        orderList.sort(Order::compareTo);
        return orderList;
    }

    private Set<Date> getUniqueDatesFromPurchaseList(List<Purchase> purchaseList) {
        Set<Date> dates = new LinkedHashSet<>();
        for (Purchase purchase : purchaseList) {
            dates.add(purchase.getDate());
        }
        return dates;
    }

    private void sortPurchaseListByStatus(List<Purchase> purchaseList) {
        purchaseList.sort((o1, o2) -> o1.getPrice().compareTo(o2.getPrice()));
    }
}
