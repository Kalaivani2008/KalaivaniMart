package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.ReviewDAO;
import com.kalaivani.mart.dao.ReviewDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
                    "DataSource not available."
            );
        }

        reviewDAO = new ReviewDAOImpl(dataSource);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // User must be logged in
        if (session == null ||
                session.getAttribute("userId") == null) {

            response.sendRedirect(
                    request.getContextPath() + "/login.jsp"
            );
            return;
        }

        try {

            // Get logged-in user
            long userId =
                    Long.parseLong(
                            session.getAttribute("userId").toString()
                    );

            // Get form values
            String productIdParam =
                    request.getParameter("productId");

            String ratingParam =
                    request.getParameter("rating");

            String comment =
                    request.getParameter("comment");

            // Basic validation
            if (productIdParam == null ||
                    ratingParam == null) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Product ID and rating are required."
                );
                return;
            }

            long productId =
                    Long.parseLong(productIdParam);

            int rating =
                    Integer.parseInt(ratingParam);

            // Rating validation
            if (rating < 1 || rating > 5) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Rating must be between 1 and 5."
                );
                return;
            }

            // Comment validation
            if (comment == null) {
                comment = "";
            }

            comment = comment.trim();

            if (comment.length() > 1000) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Comment is too long."
                );
                return;
            }

            /*
             * IMPORTANT:
             * Only buyers who have a DELIVERED order
             * containing this product can review it.
             */
            boolean canReview =
                    reviewDAO.canReviewProduct(
                            userId,
                            productId
                    );

            if (!canReview) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "You can review only products from completed orders."
                );
                return;
            }

            // Add review
            reviewDAO.addReview(
                    productId,
                    userId,
                    rating,
                    comment
            );

            // Return to products page
            response.sendRedirect(
                    request.getContextPath() + "/products"
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product ID or rating."
            );

        } catch (Exception e) {

            throw new ServletException(
                    "Unable to submit review.",
                    e
            );
        }
    }
}