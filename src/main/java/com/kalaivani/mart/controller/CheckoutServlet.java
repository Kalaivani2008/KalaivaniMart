package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.OrderDAO;
import com.kalaivani.mart.dao.OrderDAOImpl;
import com.kalaivani.mart.model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.IOException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private OrderDAO orderDAO;


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

        orderDAO =
                new OrderDAOImpl(dataSource);
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


            long buyerId =
                    Long.parseLong(
                            session.getAttribute("userId")
                                    .toString()
                    );


            Order order =
                    orderDAO.createOrderFromCart(
                            buyerId
                    );


            request.setAttribute(
                    "order",
                    order
            );


            request.getRequestDispatcher(
                    "/order-success.jsp"
            ).forward(
                    request,
                    response
            );


        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "error",
                    "Checkout failed. Please try again."
            );

            request.getRequestDispatcher(
                    "/cart.jsp"
            ).forward(
                    request,
                    response
            );
        }
    }
}