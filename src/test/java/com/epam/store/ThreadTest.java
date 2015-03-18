package com.epam.store;

import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ThreadTest {
    private static final Logger log = LoggerFactory.getLogger(ThreadTest.class);
    private static final int AMOUNT_OF_CONNECTIONS_TO_GET = 200;
    private static final int TIME_OF_HOLDING_CONNECTION = 5; //sec
    private static final ConnectionPool cp = new SqlConnectionPool();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(AMOUNT_OF_CONNECTIONS_TO_GET);
        int successfulConnectionsCount = 0;
        for (int i = 0; i < AMOUNT_OF_CONNECTIONS_TO_GET; i++) {
            //create thread and get connection in the thread
            Future<SqlPooledConnection> future = executor.submit(cp::getConnection);
            SqlPooledConnection connection = future.get();
            //test the connection
            if (connection.isValid()) successfulConnectionsCount++;
            //close connection after some time
            scheduledExecutorService.schedule(connection::close, TIME_OF_HOLDING_CONNECTION, TimeUnit.SECONDS);
        }
        log.info("connections received: " + successfulConnectionsCount);
        log.info("shutdown");
        executor.shutdown();
        scheduledExecutorService.shutdown();
        cp.shutdown();
    }
}
