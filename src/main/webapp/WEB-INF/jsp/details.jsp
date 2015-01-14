<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title></title>
</head>
<body>
<body>
<div id="main_container">
    <page:logo/>
    <div id="main_content">
        <page:menuTab/>
        <page:sideBar/>
        <page:productDetails product="${param.product}"/>
    </div>
    <page:footer/>
</div>
</body>
</body>
</html>
