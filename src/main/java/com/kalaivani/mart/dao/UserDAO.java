package com.kalaivani.mart.dao;

public interface UserDAO {

    boolean emailExists(String email) throws Exception;

    void registerUser(
            String name,
            String email,
            String passwordHash,
            String role
    ) throws Exception;
    String[]loginUser(String email)throws Exception;
}