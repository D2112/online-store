<%@ tag description="Writes the HTML code for adding to cart button" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="product" type="com.epam.store.model.Product" %>

<div>
    <form method="POST" action="<c:url value="/addToCart"/>">
        <input type="hidden" name="id" value="${product.id}">
        <button type="submit" class="prod_buy">
            <fmt:message key="product.button.add"/>
        </button>
    </form>
</div>