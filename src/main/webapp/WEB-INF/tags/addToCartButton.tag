<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Writes the HTML code for adding to cart button" %>
<%@ attribute name="product" type="com.epam.store.model.Product" %>

<div>
    <form method="POST" action="<c:url value="/addToCart"/>">
        <input type="hidden" name="id" value="${product.id}">
        <button type="submit" class="prod_buy">Add to cart</button>
    </form>
</div>