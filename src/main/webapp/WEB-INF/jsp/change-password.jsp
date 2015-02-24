<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title><fmt:message key="title.changePassword"/></title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/static/css/registration-style.css"/>"/>
</head>
<body>
<div align="center" class="registration_box">
    <h1><fmt:message key="change-password.label.header"/></h1>
    <hr>
    <fmt:message key="change-password.placeholder.oldPassword" var="oldPasswordPlaceholder"/>
    <fmt:message key="change-password.placeholder.newPassword" var="newPasswordPlaceholder"/>
    <fmt:message key="change-password.placeholder.confirmNewPassword" var="confirmNewPasswordPlaceholder"/>
    <form action="<c:url value="changePassword"/>" method="post">
        <input type="password" name="oldPassword" placeholder="${oldPasswordPlaceholder}" required="true"/>
        <input type="password" name="newPassword" placeholder="${newPasswordPlaceholder}" required="true"/>
        <input type="password" name="confirmNewPassword" placeholder="${confirmNewPasswordPlaceholder}"
               required="true"/>
        <span><button type="submit" class="button"><fmt:message key="change-password.button.submit"/></button></span>
    </form>
    <c:forEach items="${errors}" var="error">
        <br/>
        <h4><span class="error_message">${error}</span></h4>
    </c:forEach>
</div>
</body>
</html>
