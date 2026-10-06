<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login - Kalaivani Mart</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            width: 400px;
            margin: 80px auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        h2 {
            text-align: center;
            margin-bottom: 25px;
        }

        label {
            display: block;
            margin-top: 15px;
            margin-bottom: 5px;
        }

        input {
            width: 100%;
            padding: 10px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        button {
            width: 100%;
            margin-top: 20px;
            padding: 12px;
            border: none;
            border-radius: 5px;
            background-color: #333;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background-color: #555;
        }

        .error {
            color: red;
            text-align: center;
            margin-bottom: 15px;
        }

        .register {
            text-align: center;
            margin-top: 20px;
        }

        .register a {
            text-decoration: none;
        }
    </style>
</head>

<body>

<div class="container">

    <h2>Login - Kalaivani Mart</h2>

    <%
        String error = (String) request.getAttribute("error");

        if (error != null) {
    %>
        <div class="error">
            <%= error %>
        </div>
    <%
        }
    %>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <label for="email">Email</label>
        <input
                type="email"
                id="email"
                name="email"
                required
        >

        <label for="password">Password</label>
        <input
                type="password"
                id="password"
                name="password"
                required
        >

        <button type="submit">Login</button>

    </form>

    <div class="register">
        Don't have an account?
        <a href="${pageContext.request.contextPath}/register.jsp">
            Register here
        </a>
    </div>

</div>

</body>
</html>