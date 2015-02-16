package com.epam.store.servlet;

import com.epam.store.model.Image;
import com.epam.store.service.ImageService;

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
    private static final String IMAGE_SERVICE_ATTRIBUTE_NAME = ImageService.class.getSimpleName();
    private ImageService imageService;

    public void init(ServletConfig config) throws ServletException {
        imageService = (ImageService) config.getServletContext().getAttribute(IMAGE_SERVICE_ATTRIBUTE_NAME);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext webContext = new WebContext(request, response);
        List<String> parametersFromURI = webContext.getParametersFromPath();
        String stringImageID = null;
        if (parametersFromURI.size() == 1) {
            stringImageID = parametersFromURI.iterator().next();
        }
        if (stringImageID == null) {
            webContext.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        Image image = imageService.getImage(Long.valueOf(stringImageID));
        if (image == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        response.reset(); //clear buffer to prevent it exceed
        response.setContentType(image.getContentType());
        response.setContentLength(image.getContent().length);
        // Write image content to response.
        response.getOutputStream().write(image.getContent());
    }
}
