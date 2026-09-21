package com.kalaivani.mart.service;

import com.kalaivani.mart.dao.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void register(
            String name,
            String email,
            String password,
            String role
    ) throws Exception {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException(
                    "Password must contain at least 6 characters"
            );
        }

        if (!role.equals("BUYER") && !role.equals("SELLER")) {
            throw new IllegalArgumentException("Invalid role");
        }

        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException(
                    "Email already registered"
            );
        }

        String passwordHash =
                BCrypt.hashpw(password, BCrypt.gensalt(10));

        userDAO.registerUser(
                name,
                email,
                passwordHash,
                role
        );
    }
}