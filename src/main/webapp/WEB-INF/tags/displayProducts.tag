<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ tag description="Takes products from the collection and write HTML code to display all of them" %>
<%@ attribute name="products" required="true" type="java.util.Collection" %>


<div class="center_content">
    <c:forEach items="${products}" var="product">
        <page:displayProductBox product="${product}" imagePath="../../static/img/p1.jpg"/>
    </c:forEach>
</div>