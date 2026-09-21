package com.kalaivani.mart.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO {

    private final DataSource dataSource;

    public UserDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean emailExists(String email) throws Exception {

        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();

                return resultSet.getInt(1) > 0;
            }
        }
    }

    @Override
    public void registerUser(
            String name,
            String email,
            String passwordHash,
            String role
    ) throws Exception {

        String sql = """
                INSERT INTO users
                (name, email, password_hash, role)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.setString(4, role);

            statement.executeUpdate();
        }
    }

    @Override
    public String[] loginUser(String email) throws Exception {

        String sql =
                "SELECT id, name, password_hash, role " +
                "FROM users WHERE email = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    return new String[] {
                            String.valueOf(
                                    resultSet.getLong("id")
                            ),
                            resultSet.getString("name"),
                            resultSet.getString("password_hash"),
                            resultSet.getString("role")
                    };
                }

                return null;
            }
        }
    }
}