package com.kalaivani.mart.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        HttpSession session =
                httpRequest.getSession(false);


        // Not logged in
        if (session == null ||
            session.getAttribute("user") == null) {

            httpResponse.sendRedirect(
                    httpRequest.getContextPath()
                            + "/login.jsp"
            );

            return;
        }


        String role =
                (String) session.getAttribute("role");

        String uri =
                httpRequest.getRequestURI();

        String contextPath =
                httpRequest.getContextPath();


        // Remove context path
        String path =
                uri.substring(contextPath.length());


        // =========================
        // SELLER AREA
        // =========================

        if (path.startsWith("/seller/")) {

            if (!"SELLER".equals(role)) {

                httpResponse.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Seller access required"
                );

                return;
            }
        }


        // =========================
        // ADMIN AREA
        // =========================

        if (path.startsWith("/admin/")) {

            if (!"ADMIN".equals(role)) {

                httpResponse.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Admin access required"
                );

                return;
            }
        }


        // Access allowed
        chain.doFilter(
                request,
                response
        );
    }
}