<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag description="Writes the HTML code for inserting a product view."%>
<%@ attribute name="product" type="com.epam.store.model.Product" %>
<%@ attribute name="imagePath"%>

<div class="prod_box">
    <div class="center_prod_box">
        <div class="product_title">
            <a href="<c:url value="..."/>"> <c:out value="${product.name}"/> </a>
        </div>
        <div class="product_img">
            <a href="<c:url value="..."/>"> <img src="<c:out value="${imagePath}"/>" border="0" /> </a>
        </div>
        <div class="prod_price">
            <span class="value">${product.price.value}</span>
        </div>
    </div>
    <div class="prod_details_tab">
        <a href="<c:url value="..."/>" class="prod_buy">Add to Cart</a>
        <a href="<c:url value="..."/>" class="prod_details">Details</a>
    </div>
</div>