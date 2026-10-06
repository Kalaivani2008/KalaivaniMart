<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Products - Stationery Mart</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
        }

        h1 {
            color: #333;
        }

        .nav {
            margin-bottom: 20px;
        }

        .nav a {
            margin-right: 15px;
        }

        .search-box {
            border: 1px solid #ccc;
            padding: 15px;
            margin-bottom: 25px;
        }

        .product {
            border: 1px solid #ccc;
            padding: 20px;
            margin-bottom: 20px;
            width: 650px;
        }

        .product h2 {
            margin-top: 0;
        }

        .review-box {
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px solid #ddd;
        }

        textarea {
            width: 500px;
            height: 80px;
        }

        button {
            padding: 7px 14px;
            cursor: pointer;
        }

        .error {
            color: red;
        }

        .success {
            color: green;
        }
    </style>
</head>

<body>

<h1>Stationery Mart - Products</h1>

<div class="nav">
    <a href="${pageContext.request.contextPath}/">Home</a>
    |
    <a href="${pageContext.request.contextPath}/cart">My Cart</a>
    |
    <a href="${pageContext.request.contextPath}/orders">My Orders</a>
</div>

<hr>

<h2>Search Products</h2>

<div class="search-box">

    <form method="get"
          action="${pageContext.request.contextPath}/products">

        <label>Search:</label>

        <input type="text"
               name="keyword"
               value="${keyword}"
               placeholder="Search product">

        <label>Category:</label>

        <input type="text"
               name="category"
               value="${category}"
               placeholder="Category">

        <button type="submit">
            Search
        </button>

    </form>

</div>

<h2>Available Products</h2>

<c:choose>

    <c:when test="${empty products}">

        <p>No products found.</p>

    </c:when>

    <c:otherwise>

        <c:forEach var="product" items="${products}">

            <div class="product">

                <h2>
                    <c:out value="${product.name}" />
                </h2>

                <p>
                    <strong>Description:</strong>
                    <c:out value="${product.description}" />
                </p>

                <p>
                    <strong>Price:</strong>
                    ₹<c:out value="${product.price}" />
                </p>

                <p>
                    <strong>Category:</strong>
                    <c:out value="${product.category}" />
                </p>

                <p>
                    <strong>Stock:</strong>
                    <c:out value="${product.stockQty}" />
                </p>

                <!-- Add to Cart -->

                <c:choose>

                    <c:when test="${product.stockQty > 0}">

                        <form method="post"
                              action="${pageContext.request.contextPath}/cart">

                            <input type="hidden"
                                   name="productId"
                                   value="${product.id}">

                            <button type="submit">
                                Add to Cart
                            </button>

                        </form>

                    </c:when>

                    <c:otherwise>

                        <p class="error">
                            Out of Stock
                        </p>

                    </c:otherwise>

                </c:choose>


                <!-- Review -->

                <div class="review-box">

                    <h3>Write a Review</h3>

                    <form method="post"
                          action="${pageContext.request.contextPath}/review">

                        <input type="hidden"
                               name="productId"
                               value="${product.id}">

                        <label>Rating:</label>

                        <select name="rating" required>

                            <option value="">Select</option>
                            <option value="1">1 Star</option>
                            <option value="2">2 Stars</option>
                            <option value="3">3 Stars</option>
                            <option value="4">4 Stars</option>
                            <option value="5">5 Stars</option>

                        </select>

                        <br><br>

                        <label>Comment:</label>

                        <br>

                        <textarea name="comment"
                                  maxlength="1000"
                                  placeholder="Write your review"
                                  required></textarea>

                        <br><br>

                        <button type="submit">
                            Submit Review
                        </button>

                    </form>

                    <p>
                        <small>
                            You can review a product only after your order
                            has been delivered.
                        </small>
                    </p>

                </div>

            </div>

        </c:forEach>

    </c:otherwise>

</c:choose>

</body>
</html>