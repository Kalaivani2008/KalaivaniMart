package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.UserDAO;
import com.kalaivani.mart.dao.UserDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {

        DataSource dataSource =
                (DataSource) getServletContext()
                        .getAttribute("dataSource");

        if (dataSource == null) {
            throw new ServletException(
                    "DataSource not available"
            );
        }

        userDAO = new UserDAOImpl(dataSource);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Basic validation
        if (email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/login.jsp?error=Please+enter+email+and+password"
            );
            return;
        }

        try {

            String[] user =
                    userDAO.login(email.trim(), password);

            // Invalid login
            if (user == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/login.jsp?error=Invalid+email+or+password"
                );
                return;
            }

            /*
             * user[0] = user id
             * user[1] = user name
             * user[2] = password hash
             * user[3] = role
             */

            HttpSession session = request.getSession(true);

            session.setAttribute(
                    "userId",
                    Long.parseLong(user[0])
            );

            session.setAttribute(
                    "user",
                    user[1]
            );

            session.setAttribute(
                    "role",
                    user[3]
            );

            System.out.println(
                    "LOGIN SUCCESS - SESSION ID = "
                            + session.getId()
            );

            System.out.println(
                    "LOGIN USER ID = "
                            + user[0]
            );

            System.out.println(
                    "LOGIN ROLE = "
                            + user[3]
            );

            /*
             * Role-based redirect
             */

            if ("SELLER".equalsIgnoreCase(user[3])) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/seller/products"
                );

            } else if ("ADMIN".equalsIgnoreCase(user[3])) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/admin"
                );

            } else {

                // BUYER
                response.sendRedirect(
                        request.getContextPath()
                                + "/products"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            throw new ServletException(
                    "Login failed",
                    e
            );
        }
    }
}