<!DOCTYPE html>
<html>
<head>
    <title>Login - Stationery Mart</title>
</head>

<body>

<h2>Stationery Mart - Login</h2>

<form method="post" action="<%= request.getContextPath() %>/login">

    <label>Email:</label>
    <input type="email" name="email" required>

    <br><br>

    <label>Password:</label>
    <input type="password" name="password" required>

    <br><br>

    <button type="submit">Login</button>

</form>

<br>

<a href="register.jsp">Create a new account</a>

</body>
</html>