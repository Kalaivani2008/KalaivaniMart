package com.kalaivani.mart.controller;

import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

@WebServlet("/reset-seller-password")
public class ResetSellerPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            DataSource dataSource =
                    (DataSource) getServletContext()
                            .getAttribute("dataSource");

            String newPassword = "Seller@123";

            String passwordHash =
                    BCrypt.hashpw(
                            newPassword,
                            BCrypt.gensalt(10)
                    );

            String sql =
                    "UPDATE users SET password_hash = ? " +
                    "WHERE email = ? AND role = 'SELLER'";

            try (Connection connection =
                         dataSource.getConnection();
                 PreparedStatement ps =
                         connection.prepareStatement(sql)) {

                ps.setString(1, passwordHash);
                ps.setString(2, "seller@stationerymart.com");

                int rows = ps.executeUpdate();

                response.setContentType("text/html");
                response.getWriter().println(
                        "<h2>Seller password reset successful!</h2>"
                );
                response.getWriter().println(
                        "<p>Seller: seller@stationerymart.com</p>"
                );
                response.getWriter().println(
                        "<p>Password: Seller@123</p>"
                );
                response.getWriter().println(
                        "<p>Rows updated: " + rows + "</p>"
                );
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}