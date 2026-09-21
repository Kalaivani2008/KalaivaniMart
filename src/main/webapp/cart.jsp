<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="com.kalaivani.mart.model.Product" %>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>My Cart - Stationery Mart</title>

</head>

<body>

<h1>My Cart</h1>

<p>
    <a href="<%= request.getContextPath() %>/products">
        Continue Shopping
    </a>
</p>

<hr>

<%
    List<Product> products =
            (List<Product>) request.getAttribute("products");

    if (products != null && !products.isEmpty()) {

        double total = 0;
%>

<h2>Cart Items</h2>

<%
        for (Product product : products) {

            total += product.getPrice();
%>

<div style="
    border: 1px solid #999;
    padding: 15px;
    margin: 10px 0;
    width: 450px;
">

    <h3>
        <%= product.getName() %>
    </h3>

    <p>
        <strong>Description:</strong>
        <%= product.getDescription() %>
    </p>

    <p>
        <strong>Price:</strong>
        ₹<%= product.getPrice() %>
    </p>

    <p>
        <strong>Category:</strong>
        <%= product.getCategory() %>
    </p>


    <!-- Remove from Cart -->

    <form method="post"
          action="<%= request.getContextPath() %>/cart">

        <input type="hidden"
               name="action"
               value="remove">

        <input type="hidden"
               name="productId"
               value="<%= product.getId() %>">

        <button type="submit">
            Remove
        </button>

    </form>

</div>

<%
        }
%>

<hr>

<h2>
    Total: ₹<%= total %>
</h2>


<!-- Checkout -->

<form method="post"
      action="<%= request.getContextPath() %>/checkout">

    <button type="submit">
        Checkout
    </button>

</form>


<%
    } else {
%>

<h2>Your cart is empty.</h2>

<p>
    <a href="<%= request.getContextPath() %>/products">
        Browse Products
    </a>
</p>

<%
    }
%>

</body>

</html>