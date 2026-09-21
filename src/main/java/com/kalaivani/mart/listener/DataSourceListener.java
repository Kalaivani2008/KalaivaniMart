package com.kalaivani.mart.listener;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;

public class DataSourceListener implements ServletContextListener {

    private HikariDataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            HikariConfig config = new HikariConfig();

            config.setDriverClassName("org.h2.Driver");
            config.setJdbcUrl("jdbc:h2:~/stationerymart");
            config.setUsername("sa");
            config.setPassword("");

            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);

            dataSource = new HikariDataSource(config);

            // Test database connection
            try (Connection connection = dataSource.getConnection()) {
                System.out.println("DATABASE CONNECTED SUCCESSFULLY!");
            }

            // Make DataSource available to the application
            sce.getServletContext().setAttribute(
                    "dataSource",
                    dataSource
            );

            System.out.println("H2 + HikariCP STARTED SUCCESSFULLY!");

        } catch (Exception e) {

            System.out.println("H2 + HikariCP FAILED!");
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        if (dataSource != null) {
            dataSource.close();
        }

        System.out.println("H2 + HikariCP CLOSED.");
    }
}