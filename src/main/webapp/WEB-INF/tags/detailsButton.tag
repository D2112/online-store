<%@ tag description="Writes the HTML code for adding to cart button" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="product" type="com.epam.store.model.Product" %>

<div>
    <form method="GET" action="<c:url value="/details/${product.id}"/>">
        <button type="submit" class="prod_details">
            <fmt:message key="product.button.details"/>
        </button>
    </form>
</div>