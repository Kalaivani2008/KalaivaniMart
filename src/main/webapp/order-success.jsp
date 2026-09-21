<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ page import="com.kalaivani.mart.model.Order" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Order Successful - Stationery Mart</title>
</head>

<body>

<h1>Order Placed Successfully!</h1>

<%
    Order order =
            (Order) request.getAttribute("order");
%>

<% if (order != null) { %>

    <h2>Thank you for your order!</h2>

    <p>
        <strong>Order ID:</strong>
        <%= order.getId() %>
    </p>

    <p>
        <strong>Status:</strong>
        <%= order.getStatus() %>
    </p>

    <p>
        <strong>Total Amount:</strong>
        ₹<%= order.getTotalAmount() %>
    </p>

<% } %>

<hr>

<p>
    <a href="<%= request.getContextPath() %>/products">
        Continue Shopping
    </a>
</p>
<p>
    <a href="<%= request.getContextPath() %>/orders">
        View My Orders
    </a>
</p>

</body>

</html>