<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>

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
        <page:displayProducts/>
        <page:sideFilterMenu/>
    </div>
    <page:footer/>
</div>
</body>
</html>

