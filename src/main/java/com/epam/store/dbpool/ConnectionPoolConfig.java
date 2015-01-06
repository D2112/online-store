package com.epam.store.dbpool;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

import java.util.concurrent.TimeUnit;

class ConnectionPoolConfig {
    private long connectionIdleTimeout;
    private int maxConnections;
    private int minConnections;
    private int maxIdleConnections;
    private int minIdleConnections;
    private String username;
    private String password;
    private String driver;
    private String url;

    ConnectionPoolConfig() {
        PoolConfig config = ConfigFactory.create(PoolConfig.class);
        connectionIdleTimeout = TimeUnit.MINUTES.toMillis(config.connectionIdleTimeout());
        maxConnections = config.maxConnections();
        minConnections = config.minConnections();
        maxIdleConnections = config.maxIdleConnections();
        minIdleConnections = config.minIdleConnections();
        username = config.username();
        password = config.password();
        driver = config.driver();
        url = config.url();
    }

    public long connectionIdleTimeout() {
        return connectionIdleTimeout;
    }

    public int maxConnections() {
        return maxConnections;
    }

    public int minConnections() {
        return minConnections;
    }

    public int maxIdleConnections() {
        return maxIdleConnections;
    }

    public int minIdleConnections() {
        return minIdleConnections;
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

        @DefaultValue("5")//minutes
        public long connectionIdleTimeout();

        @DefaultValue("100")
        public int maxConnections();

        @DefaultValue("5")
        public int minConnections();

        @DefaultValue("5")
        public int minIdleConnections();

        @DefaultValue("50")
        public int maxIdleConnections();
    }
}
