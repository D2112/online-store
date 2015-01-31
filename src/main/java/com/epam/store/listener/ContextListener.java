package com.epam.store.listener;

import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.JdbcDaoFactory;
import com.epam.store.dao.SqlQueryGenerator;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.metadata.DBMetadataManager;
import com.epam.store.model.Category;
import com.epam.store.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Locale;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(ContextListener.class);
    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent arg) {
        ServletContext servletContext = arg.getServletContext();
        connectionPool = new SqlConnectionPool();
        DBMetadataManager dbMetadataManager;
        try (SqlPooledConnection connection = connectionPool.getConnection()) {
            dbMetadataManager = new DBMetadataManager(connection.getMetaData());
        }
        SqlQueryGenerator sqlQueryGenerator = new SqlQueryGenerator(dbMetadataManager);
        DaoFactory daoFactory = new JdbcDaoFactory(connectionPool, sqlQueryGenerator);
        servletContext.setAttribute("daoFactory", daoFactory);

        //set services to servlet context, the class name is used as an attribute name
        servletContext.setAttribute(getNameForService(ProductService.class), new ProductService(daoFactory, sqlQueryGenerator));
        servletContext.setAttribute(getNameForService(UserService.class), new UserService(daoFactory, sqlQueryGenerator));
        servletContext.setAttribute(getNameForService(RegistrationService.class), new RegistrationService(daoFactory));
        servletContext.setAttribute(getNameForService(Authenticator.class), new Authenticator(daoFactory));

        servletContext.setAttribute("locale", new Locale("ru_RU"));

        List<Category> categories = new CategoryService(daoFactory).getCategories();
        servletContext.setAttribute("categories", categories);

    }

    @Override
    public void contextDestroyed(ServletContextEvent arg) {
        connectionPool.shutdown();
    }

    private String getNameForService(Class clazz) {
        return clazz.getSimpleName();
    }
}