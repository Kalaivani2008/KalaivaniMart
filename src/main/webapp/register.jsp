<!DOCTYPE html>
<html>
<head>
    <title>Register - Stationery Mart</title>
</head>

<body>

<h2>Stationery Mart - Register</h2>

<%
    String error = (String) request.getAttribute("error");

    if (error != null) {
%>
    <p style="color:red;"><%= error %></p>
<%
    }
%>

<form method="post" action="<%= request.getContextPath() %>/register">

    <label>Name:</label>
    <input type="text" name="name" required>
    <br><br>

    <label>Email:</label>
    <input type="email" name="email" required>
    <br><br>

    <label>Password:</label>
    <input type="password" name="password" required>
    <br><br>

    <label>Role:</label>

    <select name="role" required>
        <option value="BUYER">Buyer</option>
        <option value="SELLER">Seller</option>
    </select>

    <br><br>

    <button type="submit">Register</button>

</form>

<br>

<a href="login.jsp">Already have an account? Login</a>

</body>
</html>