package com.kalaivani.mart.controller;

import com.kalaivani.mart.dao.ProductDAO;
import com.kalaivani.mart.dao.ProductDAOImpl;
import com.kalaivani.mart.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.IOException;
import java.util.List;

@WebServlet("/seller/products")
public class ProductServlet extends HttpServlet {

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {

        DataSource dataSource =
                (DataSource) getServletContext()
                        .getAttribute("dataSource");

        if (dataSource == null) {
            throw new ServletException(
                    "DataSource not available"
            );
        }

        productDAO =
                new ProductDAOImpl(dataSource);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

            System.out.println(">>> SELLER ProductServlet DOGET CALLED <<<");

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

            String role =
                    (String) session.getAttribute("role");

            if (!"SELLER".equalsIgnoreCase(role)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Seller access required"
                );

                return;
            }

            long sellerId =
                    (Long) session.getAttribute("userId");

            List<Product> products =
                    productDAO.findBySeller(sellerId);

            request.setAttribute(
                    "products",
                    products
            );

            request.getRequestDispatcher(
                    "/seller/products.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            throw new ServletException(
                    "Unable to load seller products",
                    e
            );
        }
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

            String role =
                    (String) session.getAttribute("role");

            if (!"SELLER".equalsIgnoreCase(role)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Seller access required"
                );

                return;
            }

            long sellerId =
                    (Long) session.getAttribute("userId");

            String action =
                    request.getParameter("action");

            if ("add".equals(action)) {

                String name =
                        request.getParameter("name");

                String description =
                        request.getParameter("description");

                double price =
                        Double.parseDouble(
                                request.getParameter("price")
                        );

                int stockQty =
                        Integer.parseInt(
                                request.getParameter("stockQty")
                        );

                String category =
                        request.getParameter("category");

                Product product =
                        new Product();

                product.setSellerId(sellerId);
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setStockQty(stockQty);
                product.setCategory(category);

                productDAO.addProduct(product);

            } else if ("update".equals(action)) {

                long id =
                        Long.parseLong(
                                request.getParameter("id")
                        );

                String name =
                        request.getParameter("name");

                String description =
                        request.getParameter("description");

                double price =
                        Double.parseDouble(
                                request.getParameter("price")
                        );

                int stockQty =
                        Integer.parseInt(
                                request.getParameter("stockQty")
                        );

                String category =
                        request.getParameter("category");

                Product product =
                        new Product();

                product.setId(id);
                product.setSellerId(sellerId);
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setStockQty(stockQty);
                product.setCategory(category);

                productDAO.updateProduct(product);

            } else if ("delete".equals(action)) {

                long id =
                        Long.parseLong(
                                request.getParameter("id")
                        );

                productDAO.deleteProduct(
                        id,
                        sellerId
                );
            }

            response.sendRedirect(
                    request.getContextPath()
                            + "/seller/products"
            );

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product data"
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new ServletException(
                    "Product operation failed",
                    e
            );
        }
    }
}