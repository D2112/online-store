<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/registration-style.css"/>

<div align="center" class="registration_box">
    <h1>Sign Up</h1>

    <form action="signup" method="post">
        <hr>
        <input type="text" name="email" placeholder="Email" required/>
        <input type="password" name="password" placeholder="Password" required/>
        <button type="submit" class="button">Sign Up</button>
    </form>
    <a href="registration">or register</a>

    <h4><span style="color: red; ">${sessionScope.get("loginResult")}</span></h4>
    ${sessionScope.remove("loginResult")}

</div>