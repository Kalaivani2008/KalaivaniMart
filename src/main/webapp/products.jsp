<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="com.kalaivani.mart.model.Product" %>

<!DOCTYPE html>

<html>

<head>

    <meta charset="UTF-8">

    <title>Products - Stationery Mart</title>

</head>

<body>

<h1>Stationery Mart</h1>

<h2>Browse Products</h2>


<!-- ========================= -->
<!-- SEARCH -->
<!-- ========================= -->

<form method="get"
      action="<%= request.getContextPath() %>/products">

    <label>Search:</label>

    <input type="text"
           name="keyword"
           placeholder="Search product"
           value="<%= request.getAttribute("keyword") != null
                    ? request.getAttribute("keyword")
                    : "" %>">


    <label>Category:</label>

    <input type="text"
           name="category"
           placeholder="Category"
           value="<%= request.getAttribute("category") != null
                    ? request.getAttribute("category")
                    : "" %>">


    <button type="submit">
        Search
    </button>

</form>


<br>


<!-- ========================= -->
<!-- CART -->
<!-- ========================= -->

<p>

    <a href="<%= request.getContextPath() %>/cart">
        View Cart
    </a>

</p>


<hr>


<!-- ========================= -->
<!-- PRODUCTS -->
<!-- ========================= -->

<%
    List<Product> products =
            (List<Product>) request.getAttribute("products");

    if (products != null && !products.isEmpty()) {

        for (Product product : products) {
%>


<div style="
    border: 1px solid #999;
    padding: 20px;
    margin: 15px 0;
    width: 500px;
">


    <!-- PRODUCT DETAILS -->

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
        <strong>Stock:</strong>
        <%= product.getStockQty() %>
    </p>


    <p>
        <strong>Category:</strong>
        <%= product.getCategory() %>
    </p>


    <!-- ========================= -->
    <!-- ADD TO CART -->
    <!-- ========================= -->

    <form method="post"
          action="<%= request.getContextPath() %>/cart">

        <input type="hidden"
               name="action"
               value="add">

        <input type="hidden"
               name="productId"
               value="<%= product.getId() %>">


        <button type="submit">
            Add to Cart
        </button>

    </form>


    <hr>


    <!-- ========================= -->
    <!-- REVIEW -->
    <!-- ========================= -->

    <h4>
        Write a Review
    </h4>


    <form method="post"
          action="<%= request.getContextPath() %>/review">


        <input type="hidden"
               name="productId"
               value="<%= product.getId() %>">


        <label>
            Rating:
        </label>


        <select name="rating" required>

            <option value="5">
                5 - Excellent
            </option>

            <option value="4">
                4 - Very Good
            </option>

            <option value="3">
                3 - Good
            </option>

            <option value="2">
                2 - Average
            </option>

            <option value="1">
                1 - Poor
            </option>

        </select>


        <br>
        <br>


        <label>
            Comment:
        </label>


        <br>


        <textarea name="comment"
                  rows="4"
                  cols="40"
                  maxlength="1000"
                  placeholder="Write your review..."
                  required></textarea>


        <br>
        <br>


        <button type="submit">
            Submit Review
        </button>


    </form>


</div>


<%
        }

    } else {
%>


<h3>
    No products found.
</h3>


<%
    }
%>


</body>

</html>