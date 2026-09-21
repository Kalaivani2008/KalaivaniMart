<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="com.kalaivani.mart.model.Order" %>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Seller Orders - Stationery Mart</title>

</head>

<body>

<h1>Seller - Orders</h1>

<p>
    <a href="<%= request.getContextPath() %>/seller/products">
        Manage Products
    </a>
</p>

<hr>

<%
    List<Order> orders =
            (List<Order>) request.getAttribute("orders");

    if (orders != null && !orders.isEmpty()) {

        for (Order order : orders) {
%>

<div style="
    border:1px solid #999;
    padding:15px;
    margin:10px 0;
    width:500px;
">

    <h3>
        Order #<%= order.getId() %>
    </h3>

    <p>
        Buyer ID:
        <%= order.getBuyerId() %>
    </p>

    <p>
        Total:
        ₹<%= order.getTotalAmount() %>
    </p>

    <p>
        Current Status:
        <strong><%= order.getStatus() %></strong>
    </p>


    <form method="post"
          action="<%= request.getContextPath() %>/seller/orders">

        <input type="hidden"
               name="orderId"
               value="<%= order.getId() %>">


        <label>
            Update Status:
        </label>

        <select name="status">

            <option value="PENDING"
                <%= "PENDING".equals(order.getStatus())
                    ? "selected" : "" %>>
                PENDING
            </option>

            <option value="CONFIRMED"
                <%= "CONFIRMED".equals(order.getStatus())
                    ? "selected" : "" %>>
                CONFIRMED
            </option>

            <option value="SHIPPED"
                <%= "SHIPPED".equals(order.getStatus())
                    ? "selected" : "" %>>
                SHIPPED
            </option>

            <option value="DELIVERED"
                <%= "DELIVERED".equals(order.getStatus())
                    ? "selected" : "" %>>
                DELIVERED
            </option>

            <option value="CANCELLED"
                <%= "CANCELLED".equals(order.getStatus())
                    ? "selected" : "" %>>
                CANCELLED
            </option>

        </select>


        <button type="submit">
            Update Status
        </button>

    </form>

</div>

<%
        }

    } else {
%>

<h3>No orders found.</h3>

<%
    }
%>

</body>

</html>