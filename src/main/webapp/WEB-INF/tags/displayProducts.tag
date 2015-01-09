<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ tag description="Takes products from the collection and write HTML code to display all of them"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css"/>

<div class="center_content">
    <c:forEach var="i" begin="1" end="100">
        <page:productBox productName="Makita 156 MX" imagePath="../../images/p1.jpg"/>
    </c:forEach>
</div>