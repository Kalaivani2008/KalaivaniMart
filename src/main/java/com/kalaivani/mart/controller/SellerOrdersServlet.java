package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.OrderDAO;
import com.kalaivani.mart.dao.OrderDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.io.IOException;

@WebServlet("/seller/orders")
public class SellerOrdersServlet extends HttpServlet {

    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {

        DataSource dataSource =
                (DataSource) getServletContext()
                        .getAttribute("dataSource");

        orderDAO =
                new OrderDAOImpl(dataSource);
    }


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            request.setAttribute(
                    "orders",
                    orderDAO.getAllOrders()
            );

            request.getRequestDispatcher(
                    "/seller/orders.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse
                            .SC_INTERNAL_SERVER_ERROR
            );
        }
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            long orderId =
                    Long.parseLong(
                            request.getParameter("orderId")
                    );

            String status =
                    request.getParameter("status");


            if (!isValidStatus(status)) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid order status"
                );

                return;
            }


            orderDAO.updateOrderStatus(
                    orderId,
                    status
            );


            response.sendRedirect(
                    request.getContextPath()
                            + "/seller/orders"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse
                            .SC_INTERNAL_SERVER_ERROR
            );
        }
    }


    private boolean isValidStatus(String status) {

        return "PENDING".equals(status)
                || "CONFIRMED".equals(status)
                || "SHIPPED".equals(status)
                || "DELIVERED".equals(status)
                || "CANCELLED".equals(status);
    }
}