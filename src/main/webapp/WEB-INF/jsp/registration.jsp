<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registration-style.css"/>

<html>
<body>
<div align="center" class="registration_box">
    <h1>Registration</h1>
    <hr>
    <form action="registration" method="post">
        <input type="text" name="name" placeholder="Name" value="" required/>
        <input type="text" name="email" placeholder="Email" value="" required/>
        <input type="password" name="password" placeholder="Password" required/>
        <input type="password" name="passwordConfirm" placeholder="Confirm Password" required/>
        <span><button type="submit" class="button">Register</button></span>
    </form>
    <h4><span style="color: red; ">${SessionScope.get("registerResult")}</span></h4>
    ${sessionScope.remove("registerResult")}
    ${sessionScope.remove("name")}
    ${sessionScope.remove("email")}
</div>
</body>
</html>