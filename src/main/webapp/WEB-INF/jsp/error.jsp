<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title><fmt:message key="title.error"/> ${statusCode}</title>
</head>
<body>
<div style="text-align: center;">
    <h1>
        ERROR: ${statusCode}
    </h1>
</div>
</body>
</html>
