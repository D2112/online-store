<%@ tag description="Takes products from the collection and write HTML code to display all of them" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ attribute name="products" required="true" type="java.util.Collection" %>


<div class="center_content">
    <c:forEach items="${products}" var="product">
        <page:displayProductBox product="${product}"/>
    </c:forEach>
</div>