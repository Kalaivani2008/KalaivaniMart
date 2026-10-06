package com.kalaivani.mart.dao;

public interface UserDAO {

    boolean emailExists(String email) throws Exception;

    void registerUser(String name, String email,
                      String password, String role)
            throws Exception;

    String[] login(String email, String password)
            throws Exception;
}