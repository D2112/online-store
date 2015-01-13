<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Writes the HTML code for inserting a product view." %>
<%@ attribute name="product" type="com.epam.store.model.Product" %>
<%@ attribute name="imagePath" %>

<div class="prod_box">
    <div class="center_prod_box">
        <div class="product_title">
            <a href="..."> <c:out value="${product.name}"/> </a>
        </div>
        <div class="product_img">
            <a href="..."> <img src="<c:out value="${imagePath}"/>" border="0"/> </a>
        </div>
        <div class="prod_price">
            <span class="value">${product.price.value}</span>
        </div>
    </div>
    <div class="prod_details_tab">
        <div>
        <form method="POST" action="AddToCart">
            <input type="hidden" name="id" value="${product.id}">
            <button type="submit" class="prod_buy">Add to cart</button>
        </form>
        </div>
        <div>
        <form method="Get" action="#">
            <input type="hidden" name="id" value="${product.id}">
            <button type="submit" class="prod_details">Details</button>
        </form>
    </div>
    </div>
</div>