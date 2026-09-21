<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="com.kalaivani.mart.model.Order" %>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>My Orders - Stationery Mart</title>

</head>

<body>

<h1>My Orders</h1>

<p>
    <a href="<%= request.getContextPath() %>/products">
        Continue Shopping
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
    width:450px;
">

    <h3>
        Order #<%= order.getId() %>
    </h3>

    <p>
        <strong>Status:</strong>
        <%= order.getStatus() %>
    </p>

    <p>
        <strong>Total:</strong>
        ₹<%= order.getTotalAmount() %>
    </p>

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