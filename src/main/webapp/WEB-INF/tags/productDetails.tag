<%@ tag description="Writes the HTML code for inserting a product details view." %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="product" type="com.epam.store.model.Product" %>

<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/style.css"/>"/>
<div class="center_content">
    <div class="center_title_bar">${product.name}</div>
    <div class="prod_box_big">
        <div class="center_prod_box_big">
            <div class="product_img_big"><img src="<c:out value="/image/${product.image.id}"/>"/></div>
            <div class="details_big_box">
                <div class="product_title_big"><fmt:message key="details.label.description"/>:</div>
                <div class="specifications">
                    <span>${product.description}</span><br/>
                    <br/>
                    <c:forEach var="attribute" items="${requestScope.attributes}">
                        ${attribute.name}: <span class="blue">${attribute.valueAsString}</span><br/>
                    </c:forEach>
                    <fmt:message key="details.label.price"/>: <span class="blue"><page:price
                        value="${product.price.value}"/></span><br/>
                </div>
                <page:addToCartButton product="${product}"/>
            </div>
        </div>
    </div>
</div>
