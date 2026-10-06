package com.kalaivani.mart.dao;

import org.mindrot.jbcrypt.BCrypt;

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
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    @Override
    public void registerUser(String name,
                              String email,
                              String password,
                              String role)
            throws Exception {

        String passwordHash =
                BCrypt.hashpw(password, BCrypt.gensalt(10));

        String sql = """
                INSERT INTO users
                (name, email, password_hash, role)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            ps.setString(4, role);

            ps.executeUpdate();
        }
    }

    @Override
    public String[] login(String email,
                           String password)
            throws Exception {

        String sql = """
                SELECT id, name, password_hash, role
                FROM users
                WHERE email = ?
                """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps =
                     connection.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String storedHash =
                            rs.getString("password_hash");

                    if (BCrypt.checkpw(password, storedHash)) {

                        return new String[] {
                                String.valueOf(
                                        rs.getLong("id")
                                ),
                                rs.getString("name"),
                                storedHash,
                                rs.getString("role")
                        };
                    }
                }
            }
        }

        return null;
    }
}