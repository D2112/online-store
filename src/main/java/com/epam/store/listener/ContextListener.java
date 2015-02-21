package com.epam.store.listener;

import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.JdbcDaoFactory;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.service.CategoryService;
import com.epam.store.service.ImageService;
import com.epam.store.service.ProductService;
import com.epam.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The initializer of the application, on initialization of the context
 * creates all classes in one exemplar that need to be available through
 * whole application. Also on initializing class looks is database empty,
 * if it is, then database will be deployed from sql script.
 * On destroying of the context it'll shutdown the connection pool.
 */
@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(ContextListener.class);
    public static final String CATEGORY_LIST_ATTRIBUTE_NAME = "categories";
    private static final String SCRIPT_FILE_NAME = "online-store.sql";
    private static final String TABLE_COUNT_QUERY = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC';";
    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent arg) {
        ServletContext servletContext = arg.getServletContext();
        connectionPool = new SqlConnectionPool();
        try (SqlPooledConnection connection = connectionPool.getConnection()) {
            if (isDatabaseEmpty(connection)) {
                log.info("Database is empty. Trying to deploy with script");
                deployDatabaseFromScript(connection);
                log.info("Script deployed successfully");
            }
        } catch (SQLException | IOException e) {
            throw new ApplicationInitializationException(e);
        }
        DaoFactory daoFactory = new JdbcDaoFactory(connectionPool);
        servletContext.setAttribute("daoFactory", daoFactory);

        //set services to servlet context, the class name is used as an attribute name
        servletContext.setAttribute(getNameForService(ProductService.class), new ProductService(daoFactory));
        servletContext.setAttribute(getNameForService(UserService.class), new UserService(daoFactory));
        servletContext.setAttribute(getNameForService(ImageService.class), new ImageService(daoFactory));
        CategoryService categoryService = new CategoryService(daoFactory);
        servletContext.setAttribute(getNameForService(CategoryService.class), categoryService);

        //set categories list to application context to have access to it from everywhere
        servletContext.setAttribute(CATEGORY_LIST_ATTRIBUTE_NAME, new CopyOnWriteArrayList<>(categoryService.getCategories()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg) {
        connectionPool.shutdown();
    }

    private String getNameForService(Class clazz) {
        return clazz.getSimpleName();
    }

    private boolean isDatabaseEmpty(SqlPooledConnection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(TABLE_COUNT_QUERY);
        rs.next();
        long count = rs.getLong(1);
        return count == 0;
    }

    private void deployDatabaseFromScript(SqlPooledConnection connection) throws IOException, SQLException {
        String scriptQuery = readScriptQuery();
        Statement statement = connection.createStatement();
        statement.execute(scriptQuery);
    }

    private String readScriptQuery() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = ContextListener.class.getClassLoader().getResourceAsStream(SCRIPT_FILE_NAME)) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }
}