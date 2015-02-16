package com.epam.store.service;

import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.Image;

public class ImageService {
    private static final String IMAGE_ID_COLUMN = "IMAGE_ID";
    private DaoFactory daoFactory;

    public ImageService(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public Image getImage(long imageId) {
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Image> dao = daoSession.getDao(Image.class);
            return dao.findFirstByParameter(IMAGE_ID_COLUMN, imageId);
        }
    }
}
