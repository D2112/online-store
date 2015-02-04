<%@ tag description="Writes the HTML code for inserting a product view." %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="product" type="com.epam.store.model.Product" %>

<div class="prod_box">
    <div class="center_prod_box">
        <div class="product_title">
            <c:out value="${product.name}"/>
        </div>
        <div class="product_img">
            <img src="<c:out value="/image/${product.image.id}"/>" border="0"/>
        </div>
        <page:price value="${product.price.value}"/>
    </div>
    <div class="prod_details_tab">
        <page:addToCartButton product="${product}"/>
        <div>
            <form method="GET" action="<c:url value="/details"/>">
                <input type="hidden" name="id" value="${product.id}">
                <button type="submit" class="prod_details">
                    <fmt:message key="product.button.details"/>
                </button>
            </form>
        </div>
    </div>
</div>