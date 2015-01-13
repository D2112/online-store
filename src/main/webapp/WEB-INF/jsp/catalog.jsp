<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<html>
<head>
    <title>Tools Shop</title>
</head>
<body>
<div id="main_container">
    <page:logo/>
    <div id="main_content">
        <page:menuTab/>
        <page:sideBar/>
        <page:displayProducts products="${param.products}"/>
        <page:sideFilterMenu/>
    </div>
    <page:footer/>
</div>
</body>
</html>

