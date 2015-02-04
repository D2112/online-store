<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/registration-style.css"/>"/>

<div align="center" class="registration_box">
    <h1><fmt:message key="login.label.header"/></h1>

    <fmt:message key="login.placeholder.email" var="emailPlaceholder"/>
    <fmt:message key="login.placeholder.password" var="passwordPlaceholder"/>
    <form action="<c:url value="/login"/>" method="post">
        <hr>
        <input type="text" name="email" placeholder="${emailPlaceholder}" value="<c:out value="${email}"/>"
               required/>
        <input type="password" name="password" placeholder="${passwordPlaceholder}" required/>
        <button type="submit" class="button"><fmt:message key="login.button.submit"/></button>
    </form>
    <a href="<c:url value="/registration"/>"><fmt:message key="login.label.register"/></a>

    <h4><span style="color: red; ">${loginResult}</span></h4>

</div>