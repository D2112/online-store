package com.epam.store.servlet;

import com.epam.store.model.Image;
import com.epam.store.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@WebServlet(name = "ImageServlet", urlPatterns = "/image/*")
public class ImageServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ImageServlet.class);
    private static final String IMAGE_SERVICE_ATTRIBUTE_NAME = ImageService.class.getSimpleName();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    private ImageService imageService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        imageService = (ImageService) config.getServletContext().getAttribute(IMAGE_SERVICE_ATTRIBUTE_NAME);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext webContext = new WebContext(request, response);
        String stringImageID = webContext.getFirstParameterFromPath();
        if (stringImageID == null) {
            webContext.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        Image image = imageService.getImage(Long.valueOf(stringImageID));
        if (image == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
            return;
        }
        //compare cached version of image if such present
        String dateString = request.getHeader("If-Modified-Since");
        Date imageLastModifiedDate = new Date(image.getLastModified().getTime());
        if (dateString != null) {
            Date cacheLastModifiedDate = null;
            try {
                cacheLastModifiedDate = DATE_FORMAT.parse(dateString);
            } catch (ParseException e) {
                throw new DateParsingException(e);
            }
            if (imageLastModifiedDate.equals(cacheLastModifiedDate)) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED); //304
                return;
            }
        }
        response.setDateHeader("Last-Modified", imageLastModifiedDate.getTime());
        response.setContentType(image.getContentType());
        response.setContentLength(image.getContent().length);
        response.getOutputStream().write(image.getContent()); // Write image content to response.
    }
}
