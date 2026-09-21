package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.UserDAO;
import com.kalaivani.mart.dao.UserDAOImpl;
import org.mindrot.jbcrypt.BCrypt;

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

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {

            DataSource dataSource =
                    (DataSource) getServletContext()
                            .getAttribute("dataSource");

            UserDAO userDAO =
                    new UserDAOImpl(dataSource);

            String[] user =
                    userDAO.loginUser(email);

            if (user != null &&
                BCrypt.checkpw(password, user[2])) {

                // Remove old session
                HttpSession oldSession =
                        request.getSession(false);

                if (oldSession != null) {
                    oldSession.invalidate();
                }

                // Create fresh session
                HttpSession session =
                        request.getSession(true);

                session.setAttribute(
                        "userId",
                        user[0]
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
                                + session.getAttribute("userId")
                );

                response.sendRedirect(
                        request.getContextPath()
                                + "/index.jsp"
                );

            } else {

                request.setAttribute(
                        "error",
                        "Invalid email or password"
                );

                request.getRequestDispatcher(
                        "/login.jsp"
                ).forward(request, response);
            }

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Login failed. Please try again."
            );

            request.getRequestDispatcher(
                    "/login.jsp"
            ).forward(request, response);
        }
    }
}