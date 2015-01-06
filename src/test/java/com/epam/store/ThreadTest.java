package com.epam.store;

import com.epam.store.dbpool.SqlConnectionPool;
import com.epam.store.dbpool.SqlPooledConnection;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class ThreadTest {
    static SqlConnectionPool cp = new SqlConnectionPool();
    static List<SqlPooledConnection> list = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer(true);
        timer.schedule(new Releaser(), 5000, 5000);

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                SqlPooledConnection pooledConnection = cp.getConnection();
                if (pooledConnection != null) {
                    list.add(cp.getConnection());
                    System.out.println("added connection");
                }
            }).start();
        }
    }

    static class Releaser extends TimerTask {

        @Override
        public void run() {
            System.out.println("Collect");
            for (SqlPooledConnection pc : list) {
                pc.close();
            }
        }
    }
}
