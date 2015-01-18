<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<page:genericPage>
    <jsp:attribute name="leftSideBar">
        <page:userSideBar/>
    </jsp:attribute>
    <jsp:body>
        <page:productDetails product="${param.product}"/>
    </jsp:body>
</page:genericPage>