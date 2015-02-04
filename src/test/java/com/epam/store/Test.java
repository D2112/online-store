package com.epam.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;


public class Test {
    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) throws SQLException, IOException, XMLStreamException, SAXException {
/*        ResourceBundle labels = ResourceBundle.getBundle("i18n.MessagesBundle", new Locale("ru"));
        System.out.println(labels.getString("adding-category.message.emptyCategory"));
        System.out.println(Locale.ENGLISH);*/


        Path path = Paths.get("C:/rus1.jpg");
        byte[] image = Files.readAllBytes(path);
        System.out.println(image.length);
        byte[] resize = Images.resize(image, 256, 256);
        System.out.println(resize.length);



/*        ConnectionPool cp = new SqlConnectionPool();
        SqlQueryGenerator sqlQueryGenerator;
        try(SqlPooledConnection connection = cp.getConnection()){
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            DBMetadataManager dbMetadataManager = new DBMetadataManager(databaseMetaData);
            sqlQueryGenerator = new SqlQueryGenerator(dbMetadataManager);

            ResultSet indexInfo = databaseMetaData.getIndexInfo(null, null, "CATEGORY", true, true);
            while(indexInfo.next()) {
                System.out.println(indexInfo.getString("COLUMN_NAME"));
                System.out.println(indexInfo.getBoolean("NON_UNIQUE"));

            }
        }



        DaoFactory daoFactory = new JdbcDaoFactory(cp, sqlQueryGenerator);
        try(DaoSession daoSession = daoFactory.getDaoSession()) {

        }
        cp.shutdown();*/
    }
}
