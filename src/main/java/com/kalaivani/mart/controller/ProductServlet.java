package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.ProductDAO;
import com.kalaivani.mart.dao.ProductDAOImpl;
import com.kalaivani.mart.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;

@WebServlet("/seller/products")
public class ProductServlet extends HttpServlet {

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

            request.setAttribute(
                    "products",
                    productDAO.findAll()
            );

            request.getRequestDispatcher(
                    "/seller/products.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String action = request.getParameter("action");

            if ("add".equals(action)) {

                addProduct(request);

            } else if ("update".equals(action)) {

                updateProduct(request);

            } else if ("delete".equals(action)) {

                long id = Long.parseLong(
                        request.getParameter("id")
                );

                productDAO.deleteProduct(id);
            }

            response.sendRedirect(
                    request.getContextPath() + "/seller/products"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
        }
    }

    private void addProduct(
            HttpServletRequest request)
            throws Exception {

        HttpServletRequest req = request;

        long sellerId = Long.parseLong(
                (String) req.getSession()
                        .getAttribute("userId")
        );

        Product product = new Product();

        product.setSellerId(sellerId);
        product.setName(req.getParameter("name"));
        product.setDescription(req.getParameter("description"));
        product.setPrice(
                Double.parseDouble(req.getParameter("price"))
        );
        product.setStockQty(
                Integer.parseInt(req.getParameter("stockQty"))
        );
        product.setCategory(req.getParameter("category"));

        productDAO.addProduct(product);
    }

    private void updateProduct(
            HttpServletRequest request)
            throws Exception {

        Product product = new Product();

        product.setId(
                Long.parseLong(request.getParameter("id"))
        );

        product.setName(request.getParameter("name"));
        product.setDescription(
                request.getParameter("description")
        );

        product.setPrice(
                Double.parseDouble(
                        request.getParameter("price")
                )
        );

        product.setStockQty(
                Integer.parseInt(
                        request.getParameter("stockQty")
                )
        );

        product.setCategory(
                request.getParameter("category")
        );

        productDAO.updateProduct(product);
    }
}