package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.ProductDAO;
import com.kalaivani.mart.dao.ProductDAOImpl;
import com.kalaivani.mart.dao.ReviewDAO;
import com.kalaivani.mart.dao.ReviewDAOImpl;
import com.kalaivani.mart.model.Product;
import com.kalaivani.mart.model.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/products")
public class BrowseProductsServlet extends HttpServlet {

    private ProductDAO productDAO;
    private ReviewDAO reviewDAO;

    @Override
    public void init() throws ServletException {

        DataSource dataSource =
                (DataSource) getServletContext()
                        .getAttribute("dataSource");

        if (dataSource == null) {
            throw new ServletException("DataSource not available");
        }

        productDAO = new ProductDAOImpl(dataSource);
        reviewDAO = new ReviewDAOImpl(dataSource);
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

            List<Product> products =
                    productDAO.searchProducts(
                            keyword,
                            category
                    );

            Map<Long, List<Review>> reviewsByProduct =
                    new HashMap<>();

            for (Product product : products) {

                reviewsByProduct.put(
                        product.getId(),
                        reviewDAO.getReviewsByProduct(
                                product.getId()
                        )
                );
            }

            request.setAttribute(
                    "products",
                    products
            );

            request.setAttribute(
                    "reviewsByProduct",
                    reviewsByProduct
            );

            request.setAttribute(
                    "keyword",
                    keyword
            );

            request.setAttribute(
                    "category",
                    category
            );

            request.getRequestDispatcher(
                    "/products.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            throw new ServletException(
                    "Unable to load products and reviews",
                    e
            );
        }
    }
}