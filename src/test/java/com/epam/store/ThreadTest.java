package com.epam.store;

import com.epam.store.dbpool.ConnectionPool;
import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ThreadTest {
    private static final Logger log = LoggerFactory.getLogger(ThreadTest.class);
    private static final ConnectionPool cp = new SqlConnectionPool();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1000);
        for (int i = 0; i < 1000; i++) {
            Future<SqlPooledConnection> future = executor.submit(cp::getConnection);
            SqlPooledConnection connection = future.get();
            scheduledExecutorService.schedule(connection::close, 5, TimeUnit.SECONDS);
        }
        Thread.sleep(200000);
        cp.shutdown();
    }
}
