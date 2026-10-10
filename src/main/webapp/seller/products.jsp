<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Seller Product Management - Stationery Mart</title>

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

        .add-product {
            border: 2px solid #444;
            padding: 20px;
            margin-bottom: 30px;
            width: 600px;
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

        input,
        textarea {
            margin: 5px 0;
            padding: 7px;
        }

        textarea {
            width: 500px;
            height: 70px;
        }

        button {
            padding: 7px 14px;
            margin-top: 5px;
            cursor: pointer;
        }

        .delete-button {
            background-color: #eee;
        }
    </style>
</head>

<body>

<h1>Seller Product Management</h1>

<div class="nav">

    <a href="${pageContext.request.contextPath}/">
        Home
    </a>

    |

    <a href="${pageContext.request.contextPath}/seller/products">
        My Products
    </a>

    |

    <a href="${pageContext.request.contextPath}/seller/orders">
        Incoming Orders
    </a>

    |

    <a href="${pageContext.request.contextPath}/products">
        Buyer Products
    </a>

</div>

<hr>

<!-- ========================= -->
<!-- ADD NEW PRODUCT -->
<!-- ========================= -->

<h2>Add New Product</h2>

<div class="add-product">

    <form method="post"
          action="${pageContext.request.contextPath}/seller/products">

        <input type="hidden"
               name="action"
               value="add">

        <p>
            <label>Product Name:</label><br>

            <input type="text"
                   name="name"
                   required
                   maxlength="200">
        </p>

        <p>
            <label>Description:</label><br>

            <textarea name="description"
                      maxlength="1000"></textarea>
        </p>

        <p>
            <label>Price:</label><br>

            <input type="number"
                   name="price"
                   step="0.01"
                   min="0"
                   required>
        </p>

        <p>
            <label>Stock Quantity:</label><br>

            <input type="number"
                   name="stockQty"
                   min="0"
                   required>
        </p>

        <p>
            <label>Category:</label><br>

            <input type="text"
                   name="category"
                   maxlength="100"
                   required>
        </p>

        <button type="submit">
            Add Product
        </button>

    </form>

</div>

<hr>

<h2>My Products</h2>

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
                    <strong>Product ID:</strong>
                    <c:out value="${product.id}" />
                </p>

                <p>
                    <strong>Description:</strong>
                    <c:out value="${product.description}" />
                </p>

                <p>
                    <strong>Current Price:</strong>
                    ₹<c:out value="${product.price}" />
                </p>

                <p>
                    <strong>Current Stock:</strong>
                    <c:out value="${product.stockQty}" />
                </p>

                <p>
                    <strong>Category:</strong>
                    <c:out value="${product.category}" />
                </p>


                <!-- ========================= -->
                <!-- UPDATE PRODUCT -->
                <!-- ========================= -->

                <h3>Edit Product</h3>

                <form method="post"
                      action="${pageContext.request.contextPath}/seller/products">

                    <input type="hidden"
                           name="action"
                           value="update">

                    <input type="hidden"
                           name="id"
                           value="${product.id}">

                    <p>
                        <label>Name:</label><br>

                        <input type="text"
                               name="name"
                               value="${product.name}"
                               maxlength="200"
                               required>
                    </p>

                    <p>
                        <label>Description:</label><br>

                        <textarea name="description"
                                  maxlength="1000"><c:out value="${product.description}" /></textarea>
                    </p>

                    <p>
                        <label>Price:</label><br>

                        <input type="number"
                               name="price"
                               value="${product.price}"
                               step="0.01"
                               min="0"
                               required>
                    </p>

                    <p>
                        <label>Stock Quantity:</label><br>

                        <input type="number"
                               name="stockQty"
                               value="${product.stockQty}"
                               min="0"
                               required>
                    </p>

                    <p>
                        <label>Category:</label><br>

                        <input type="text"
                               name="category"
                               value="${product.category}"
                               maxlength="100"
                               required>
                    </p>

                    <button type="submit">
                        Update Product
                    </button>

                </form>


                <!-- ========================= -->
                <!-- DELETE PRODUCT -->
                <!-- ========================= -->

                <h3>Delete Product</h3>

                <form method="post"
                      action="${pageContext.request.contextPath}/seller/products">

                    <input type="hidden"
                           name="action"
                           value="delete">

                    <input type="hidden"
                           name="id"
                           value="${product.id}">

                    <button type="submit"
                            class="delete-button"
                            onclick="return confirm('Are you sure you want to delete this product?');">

                        Delete Product

                    </button>

                </form>

            </div>

        </c:forEach>

    </c:otherwise>

</c:choose>

</body>
</html>