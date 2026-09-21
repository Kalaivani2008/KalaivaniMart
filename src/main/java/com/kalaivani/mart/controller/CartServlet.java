package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.CartDAO;
import com.kalaivani.mart.dao.CartDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private CartDAO cartDAO;


    @Override
    public void init() throws ServletException {

        DataSource dataSource =
                (DataSource) getServletContext()
                        .getAttribute("dataSource");

        if (dataSource == null) {
            throw new ServletException(
                    "DataSource not found in ServletContext"
            );
        }

        cartDAO = new CartDAOImpl(dataSource);
    }


    // =========================
    // GET - View Cart
    // =========================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            HttpSession session =
                    request.getSession(false);

            if (session == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/login.jsp"
                );

                return;
            }


            Object userIdObject =
                    session.getAttribute("userId");


            System.out.println(
                    "CART GET SESSION ID = "
                            + session.getId()
            );

            System.out.println(
                    "CART GET USER ID = "
                            + userIdObject
            );


            if (userIdObject == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/login.jsp"
                );

                return;
            }


            long userId =
                    Long.parseLong(
                            userIdObject.toString()
                    );


            request.setAttribute(
                    "products",
                    cartDAO.getCartProducts(userId)
            );


            request.getRequestDispatcher(
                    "/cart.jsp"
            ).forward(
                    request,
                    response
            );


        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse
                            .SC_INTERNAL_SERVER_ERROR,
                    "Unable to load cart"
            );
        }
    }


    // =========================
    // POST - Add / Remove Cart
    // =========================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            HttpSession session =
                    request.getSession(false);

            if (session == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/login.jsp"
                );

                return;
            }


            Object userIdObject =
                    session.getAttribute("userId");


            System.out.println(
                    "CART POST SESSION ID = "
                            + session.getId()
            );

            System.out.println(
                    "CART POST USER ID = "
                            + userIdObject
            );


            if (userIdObject == null) {

                response.sendRedirect(
                        request.getContextPath()
                                + "/login.jsp"
                );

                return;
            }


            long userId =
                    Long.parseLong(
                            userIdObject.toString()
                    );


            String action =
                    request.getParameter("action");


            String productIdParameter =
                    request.getParameter("productId");


            if (action == null ||
                productIdParameter == null) {

                response.sendError(
                        HttpServletResponse
                                .SC_BAD_REQUEST,
                        "Invalid cart request"
                );

                return;
            }


            long productId =
                    Long.parseLong(
                            productIdParameter
                    );


            // Add product
            if ("add".equals(action)) {

                cartDAO.addToCart(
                        userId,
                        productId
                );

            }


            // Remove product
            else if ("remove".equals(action)) {

                cartDAO.removeFromCart(
                        userId,
                        productId
                );

            }


            // Invalid action
            else {

                response.sendError(
                        HttpServletResponse
                                .SC_BAD_REQUEST,
                        "Invalid cart action"
                );

                return;
            }


            // Redirect back to cart
            response.sendRedirect(
                    request.getContextPath()
                            + "/cart"
            );


        } catch (NumberFormatException e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse
                            .SC_BAD_REQUEST,
                    "Invalid product ID"
            );


        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse
                            .SC_INTERNAL_SERVER_ERROR,
                    "Unable to update cart"
            );
        }
    }
}