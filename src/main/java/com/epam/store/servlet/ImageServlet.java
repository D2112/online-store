package com.epam.store.servlet;

import com.epam.store.dao.Dao;
import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.DaoSession;
import com.epam.store.model.Image;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/image/*")
public class ImageServlet extends HttpServlet {
    private DaoFactory daoFactory;

    public void init(ServletConfig config) throws ServletException {
        daoFactory = (DaoFactory) config.getServletContext().getAttribute("daoFactory");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext webContext = new WebContext(request, response);
        List<String> parametersFromURI = webContext.getParametersFromURI();
        String imageId = null;
        if (parametersFromURI.size() == 1) {
            imageId = parametersFromURI.iterator().next();
        }
        if (imageId == null) {
            webContext.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        Image image = null;
        try (DaoSession daoSession = daoFactory.getDaoSession()) {
            Dao<Image> dao = daoSession.getDao(Image.class);
            List<Image> images = dao.findByParameter("IMAGE_ID", imageId);
            if (images.size() == 1) image = images.iterator().next();
        }
        if (image == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        response.reset();
        response.setContentType(image.getContentType());
        response.setContentLength(image.getContent().length);
        // Write image content to response.
        response.getOutputStream().write(image.getContent());
    }
}
