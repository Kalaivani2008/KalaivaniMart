<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html><html>
<head>
    <meta charset="UTF-8">
    <title>Stationery Mart - Admin Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f3f4f6;
            margin: 0;
            padding: 30px;
        }
        h1 { color: #243b53; }
        .card {
            background: white;
            padding: 22px;
            margin: 15px 0;
            border-radius: 10px;
            box-shadow: 0 2px 8px #ddd;
        }
        .status { color: #16803c; font-weight: bold; }
    </style>
</head>
<body><h1>Stationery Mart — Admin Dashboard</h1>

<div class="card">
    <h2>Welcome, Admin!</h2>
    <p>Admin dashboard successfully opened.</p>
    <p class="status">Dashboard: Working</p>
</div>

<div class="card">
    <h2>User Management</h2>
    <p>View and manage registered users — next step.</p>
</div>

<div class="card">
    <h2>Product Management</h2>
    <p>View and manage products — next step.</p>
</div>

<div class="card">
    <h2>Order Management</h2>
    <p>View and manage all orders — next step.</p>
</div>

<p>
    <a href="${pageContext.request.contextPath}/products">
        Back to Products
    </a>
</p>

</body>
</html>