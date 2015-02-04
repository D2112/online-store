<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/registration-style.css"/>"/>

<html>
<body>
<div align="center" class="registration_box">
    <h1><fmt:message key="registration.label.header"/></h1>
    <hr>
    <fmt:message key="registration.placeholder.name" var="namePlaceholder"/>
    <fmt:message key="registration.placeholder.email" var="emailPlaceholder"/>
    <fmt:message key="registration.placeholder.password" var="passwordPlaceholder"/>
    <fmt:message key="registration.placeholder.confirmPassword" var="confirmPasswordPlaceholder"/>
    <form action="<c:url value="/registration"/>" method="post">
        <input type="text" name="name" placeholder="${namePlaceholder}" value="<c:out value="${requestScope.name}"/>"
               required="true"/>
        <input type="text" name="email" placeholder="${emailPlaceholder}" value="<c:out value="${requestScope.email}"/>"
               required="true"/>
        <input type="password" name="password" placeholder="${passwordPlaceholder}" required="true"/>
        <input type="password" name="passwordConfirm" placeholder="${confirmPasswordPlaceholder}" required="true"/>
        <span><button type="submit" class="button">
            <fmt:message key="registration.button.submit"/>
        </button></span>
    </form>
    <h4><span class="error_message">${error}</span></h4>
</div>
</body>
</html>