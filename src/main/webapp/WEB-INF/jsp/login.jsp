<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="name" value="name"/>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/registration-style.css"/>

<div align="center" class="registration_box">
    <h1>Sign Up</h1>
    <form action="login" method="post">
        <hr>
        <input type="text" name="email" placeholder="Email" value="<c:out value="${requestScope.get(name)}"/>" required/>
        <input type="password" name="password" placeholder="Password" required/>
        <button type="submit" class="button">Log In</button>
    </form>
    <a href="registration">or register</a>

    <h4><span style="color: red; ">${requestScope.get("loginResult")}</span></h4>

</div>