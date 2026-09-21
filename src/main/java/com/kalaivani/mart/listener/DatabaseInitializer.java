package com.kalaivani.mart.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        try {

            DataSource dataSource =
                    (DataSource) event.getServletContext()
                            .getAttribute("dataSource");

            if (dataSource == null) {
                System.out.println("DATABASE INITIALIZER: DataSource not found!");
                return;
            }

            try (Connection connection = dataSource.getConnection()) {

                // Check whether our users table really exists
                if (!usersTableExists(connection)) {

                    System.out.println("Creating database tables...");

                    runSqlFile(
                            connection,
                            "/sql/schema.sql"
                    );

                    System.out.println("Database tables created successfully!");
                } else {

                    System.out.println(
                            "Database tables already exist."
                    );
                }

                // Check whether users table has data
                if (isDatabaseEmpty(connection)) {

                    System.out.println("Adding seed data...");

                    runSqlFile(
                            connection,
                            "/sql/seed.sql"
                    );

                    System.out.println(
                            "Seed data added successfully!"
                    );

                } else {

                    System.out.println(
                            "Seed data already exists."
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "DATABASE INITIALIZATION FAILED!"
            );

            e.printStackTrace();
        }
    }

    private boolean usersTableExists(Connection connection)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_SCHEMA = 'PUBLIC' " +
                "AND TABLE_NAME = 'USERS'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            resultSet.next();

            return resultSet.getInt(1) > 0;
        }
    }

    private boolean isDatabaseEmpty(Connection connection)
            throws SQLException {

        String sql = "SELECT COUNT(*) FROM USERS";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            resultSet.next();

            return resultSet.getInt(1) == 0;
        }
    }

    private void runSqlFile(
            Connection connection,
            String resourcePath
    ) throws Exception {

        InputStream inputStream =
                getClass().getResourceAsStream(resourcePath);

        if (inputStream == null) {

            throw new Exception(
                    "SQL file not found: " + resourcePath
            );
        }

        StringBuilder sql = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     inputStream,
                                     StandardCharsets.UTF_8
                             ))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }

                sql.append(line).append("\n");
            }
        }

        String[] statements =
                sql.toString().split(";");

        for (String statementText : statements) {

            statementText = statementText.trim();

            if (!statementText.isEmpty()) {

                try (Statement statement =
                             connection.createStatement()) {

                    statement.execute(statementText);
                }
            }
        }
    }
}