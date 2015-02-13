package com.epam.store.dbpool;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

import java.util.concurrent.TimeUnit;

class ConnectionPoolConfig {
    private long connectionIdleTimeout;
    private int connectionValidTimeout;
    private int maxConnections;
    private int minConnections;
    private int maxAvailableConnections;
    private int minAvailableConnections;
    private String username;
    private String password;
    private String driver;
    private String url;

    ConnectionPoolConfig() {
        PoolConfig config = ConfigFactory.create(PoolConfig.class);
        connectionIdleTimeout = TimeUnit.MINUTES.toMillis(config.connectionIdleTimeout());
        connectionValidTimeout = config.connectionValidTimeout();
        maxConnections = config.maxConnections();
        minConnections = config.minConnections();
        maxAvailableConnections = config.maxAvailableConnections();
        minAvailableConnections = config.minAvailableConnections();
        username = config.username();
        password = config.password();
        driver = config.driver();
        url = config.url();
    }

    public long connectionIdleTimeout() {
        return connectionIdleTimeout;
    }

    public int getConnectionValidTimeout() {
        return connectionValidTimeout;
    }

    public int maxConnections() {
        return maxConnections;
    }

    public int minConnections() {
        return minConnections;
    }

    public int maxAvailableConnections() {
        return maxAvailableConnections;
    }

    public int minAvailableConnections() {
        return minAvailableConnections;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String driver() {
        return driver;
    }

    public String url() {
        return url;
    }


    @Config.Sources("classpath:database.properties")
    interface PoolConfig extends Config {

        public String username();

        public String password();

        public String driver();

        public String url();

        @DefaultValue("2")//minutes
        public long connectionIdleTimeout();

        @DefaultValue("5")//sec
        public int connectionValidTimeout();

        @DefaultValue("100")
        public int maxConnections();

        @DefaultValue("5")
        public int minConnections();

        @DefaultValue("5")
        public int minAvailableConnections();

        @DefaultValue("50")
        public int maxAvailableConnections();
    }
}
