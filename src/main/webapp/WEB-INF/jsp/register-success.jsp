<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title></title>
</head>
<body>
    <div style="text-align: center;">
        <h1><fmt:message key="registration-success.label.successHeader"/></h1>
        <label>
            <a href="<c:url value="/login"/>"><fmt:message key="registration-success.label.login"/></a>
        </label>
    </div>
</body>
</html>
