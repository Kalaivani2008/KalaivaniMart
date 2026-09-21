package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.OrderDAO;
import com.kalaivani.mart.dao.OrderDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;

import java.io.IOException;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {

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

            request.setAttribute(
                    "orders",
                    orderDAO.getOrdersByBuyer(buyerId)
            );

            request.getRequestDispatcher(
                    "/orders.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse
                            .SC_INTERNAL_SERVER_ERROR
            );
        }
    }
}