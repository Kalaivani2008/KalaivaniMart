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
            background-color: #f5f5f5;
        }

        h1 {
            color: #333;
        }

        .nav {
            margin-bottom: 20px;
        }

        .nav a {
            margin-right: 10px;
            text-decoration: none;
            color: #0066cc;
        }

        .search-box {
            background: white;
            padding: 20px;
            margin-bottom: 20px;
            border-radius: 8px;
        }

        .product {
            background: white;
            padding: 20px;
            margin-bottom: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .product h2 {
            margin-top: 0;
            color: #222;
        }

        .product p {
            margin: 8px 0;
        }

        button {
            padding: 8px 15px;
            cursor: pointer;
        }

        .review {
            background: #f9f9f9;
            padding: 10px;
            margin: 10px 0;
            border-left: 4px solid #007bff;
        }

        .review-form {
            margin-top: 15px;
            padding: 15px;
            background: #f0f0f0;
        }

        textarea {
            width: 100%;
            max-width: 500px;
        }
    </style>
</head>

<body>

<h1>Stationery Mart - Products</h1>

<div class="nav">
    <a href="${pageContext.request.contextPath}/">Home</a> |
    <a href="${pageContext.request.contextPath}/cart">My Cart</a> |
    <a href="${pageContext.request.contextPath}/orders">My Orders</a>
</div>

<hr>

<!-- SEARCH PRODUCTS -->

<div class="search-box">
    <h2>Search Products</h2>

    <form method="get"
          action="${pageContext.request.contextPath}/products">

        <label>Keyword:</label>
        <input type="text"
               name="keyword"
               value="<c:out value='${keyword}'/>"
               placeholder="Search product">

        <label>Category:</label>
        <input type="text"
               name="category"
               value="<c:out value='${category}'/>"
               placeholder="Category">

        <button type="submit">Search</button>
    </form>
</div>

<!-- PRODUCT LIST -->

<c:choose>
    <c:when test="${not empty products}">

        <c:forEach var="product" items="${products}">

            <div class="product">

                <h2>
                    <c:out value="${product.name}"/>
                </h2>

                <p>
                    <strong>Description:</strong>
                    <c:out value="${product.description}"/>
                </p>

                <p>
                    <strong>Price:</strong>
                    ₹<c:out value="${product.price}"/>
                </p>

                <p>
                    <strong>Stock:</strong>
                    <c:out value="${product.stockQty}"/>
                </p>

                <p>
                    <strong>Category:</strong>
                    <c:out value="${product.category}"/>
                </p>

                <!-- ADD TO CART -->

                <form method="post"
                      action="${pageContext.request.contextPath}/cart">

                    <input type="hidden"
                           name="action"
                           value="add">

                    <input type="hidden"
                           name="productId"
                           value="${product.id}">

                    <button type="submit">
                        Add to Cart
                    </button>
                </form>

                <hr>

                <!-- CUSTOMER REVIEWS -->

                <h3>Customer Reviews</h3>

                <c:choose>

                    <c:when test="${not empty reviewsByProduct[product.id]}">

                        <c:forEach var="review"
                                   items="${reviewsByProduct[product.id]}">

                            <div class="review">

                                <p>
                                    <strong>Rating:</strong>
                                    <c:out value="${review.rating}"/> / 5
                                </p>

                                <p>
                                    <strong>Comment:</strong>
                                    <c:out value="${review.comment}"/>
                                </p>

                            </div>

                        </c:forEach>

                    </c:when>

                    <c:otherwise>
                        <p>No reviews yet.</p>
                    </c:otherwise>

                </c:choose>

                <!-- WRITE REVIEW -->

                <div class="review-form">

                    <h3>Write a Review</h3>

                    <form method="post"
                          action="${pageContext.request.contextPath}/review">

                        <input type="hidden"
                               name="productId"
                               value="${product.id}">

                        <label>Rating:</label>

                        <select name="rating" required>
                            <option value="5">5 - Excellent</option>
                            <option value="4">4 - Very Good</option>
                            <option value="3">3 - Good</option>
                            <option value="2">2 - Average</option>
                            <option value="1">1 - Poor</option>
                        </select>

                        <br><br>

                        <label>Comment:</label>
                        <br>

                        <textarea name="comment"
                                  rows="4"
                                  maxlength="1000"
                                  placeholder="Write your review"
                                  required></textarea>

                        <br><br>

                        <button type="submit">
                            Submit Review
                        </button>

                    </form>

                </div>

            </div>

        </c:forEach>

    </c:when>

    <c:otherwise>
        <p>No products found.</p>
    </c:otherwise>

</c:choose>

</body>
</html>