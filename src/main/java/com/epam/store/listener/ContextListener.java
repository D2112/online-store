package com.epam.store.listener;

import com.epam.store.dao.DaoFactory;
import com.epam.store.dao.JdbcDaoFactory;
import com.epam.store.dao.SqlQueryFactory;
import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import com.epam.store.model.Category;
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
import java.util.List;
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
    private static final String SCRIPT_FILE_NAME = "online-store.sql";
    private ConnectionPool connectionPool;

    @Override
    public void contextInitialized(ServletContextEvent arg) {
        ServletContext servletContext = arg.getServletContext();
        connectionPool = new SqlConnectionPool();
        SqlQueryFactory sqlQueryFactory;
        try (SqlPooledConnection connection = connectionPool.getConnection()) {
            sqlQueryFactory = new SqlQueryFactory(connection.getMetaData());
            if (isDatabaseEmpty(connection, sqlQueryFactory)) {
                log.info("Database is empty. Trying to deploy with script");
                deployDatabaseFromScript(connection);
                //Construct query factory again with new database metadata with new information from deploying script
                sqlQueryFactory = new SqlQueryFactory(connection.getMetaData());
                log.info("Script deployed successfully");
            }
        } catch (SQLException | IOException e) {
            throw new ApplicationInitializationException(e);
        }
        DaoFactory daoFactory = new JdbcDaoFactory(connectionPool, sqlQueryFactory);
        servletContext.setAttribute("daoFactory", daoFactory);

        //set services to servlet context, the class name is used as an attribute name
        servletContext.setAttribute(getNameForService(ProductService.class), new ProductService(daoFactory, sqlQueryFactory));
        servletContext.setAttribute(getNameForService(UserService.class), new UserService(daoFactory, sqlQueryFactory));
        servletContext.setAttribute(getNameForService(ImageService.class), new ImageService(daoFactory));
        CategoryService categoryService = new CategoryService(daoFactory);
        servletContext.setAttribute(getNameForService(CategoryService.class), categoryService);

        //set categories list to application context to have access to it from everywhere
        List<Category> categories = new CopyOnWriteArrayList<>(categoryService.getCategories());
        servletContext.setAttribute("categories", categories);
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg) {
        connectionPool.shutdown();
    }

    private String getNameForService(Class clazz) {
        return clazz.getSimpleName();
    }

    private boolean isDatabaseEmpty(SqlPooledConnection connection, SqlQueryFactory sqlQueryFactory) throws SQLException {
        String publicTablesCountQuery = sqlQueryFactory.getPublicTablesCountQuery();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(publicTablesCountQuery);
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