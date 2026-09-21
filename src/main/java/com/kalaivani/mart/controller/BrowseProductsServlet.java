package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.ProductDAO;
import com.kalaivani.mart.dao.ProductDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;

@WebServlet("/products")
public class BrowseProductsServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {

        DataSource dataSource =
                (DataSource) getServletContext()
                        .getAttribute("dataSource");

        productDAO = new ProductDAOImpl(dataSource);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String keyword =
                    request.getParameter("keyword");

            String category =
                    request.getParameter("category");

            request.setAttribute(
                    "products",
                    productDAO.searchProducts(
                            keyword,
                            category
                    )
            );

            request.setAttribute("keyword", keyword);
            request.setAttribute("category", category);

            request.getRequestDispatcher(
                    "/products.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
        }
    }
}