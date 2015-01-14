<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ tag description="Writes the HTML code for inserting a right side bar." %>

<div class="right_content">
    <div class="border_box">
        <page:cartPreview cart="${sessionScope.cart}"/>
        <page:welcomeLabel user="${param.user}"/>
        <page:categories categories="${applicationScope.categories}"/>
    </div>
</div>