package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.ReviewDAO;
import com.kalaivani.mart.dao.ReviewDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.io.IOException;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private ReviewDAO reviewDAO;

    @Override
    public void init() throws ServletException {

        DataSource dataSource =
                (DataSource) getServletContext()
                        .getAttribute("dataSource");

        if (dataSource == null) {
            throw new ServletException(
                    "DataSource not found"
            );
        }

        reviewDAO =
                new ReviewDAOImpl(dataSource);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            HttpSession session =
                    request.getSession(false);

            if (session == null ||
                session.getAttribute("userId") == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/login.jsp"
                );
                return;
            }

            long userId =
                    Long.parseLong(
                            session.getAttribute("userId")
                                    .toString()
                    );

            long productId =
                    Long.parseLong(
                            request.getParameter("productId")
                    );

            int rating =
                    Integer.parseInt(
                            request.getParameter("rating")
                    );

            String comment =
                    request.getParameter("comment");


            if (rating < 1 || rating > 5) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Rating must be between 1 and 5"
                );

                return;
            }

            reviewDAO.addReview(
                    productId,
                    userId,
                    rating,
                    comment
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/products"
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid review data"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to add review"
            );
        }
    }
}