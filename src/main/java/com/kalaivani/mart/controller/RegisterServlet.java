package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.UserDAO;
import com.kalaivani.mart.dao.UserDAOImpl;
import com.kalaivani.mart.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        try {

            DataSource dataSource =
                    (DataSource) getServletContext()
                            .getAttribute("dataSource");

            UserDAO userDAO =
                    new UserDAOImpl(dataSource);

            UserService userService =
                    new UserService(userDAO);

            userService.register(
                    name,
                    email,
                    password,
                    role
            );

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());

            request.getRequestDispatcher(
                    "/register.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Registration failed. Please try again."
            );

            request.getRequestDispatcher(
                    "/register.jsp"
            ).forward(request, response);
        }
    }
}