<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ tag description="Writes the HTML code for inserting a product details view." %>
<%@ attribute name="product" type="com.epam.store.model.Product" %>
<%@ attribute name="imagePath" %>


<div class="center_content">
    <div class="center_title_bar">${product.name}</div>
    <div class="prod_box_big">
        <div class="center_prod_box_big">
            <div class="product_img_big"><img src="../../static/img/p1.jpg" alt="" border="0"/></div>
            <div class="details_big_box">
                <div class="product_title_big">Description:</div>
                <div class="specifications">
                    <c:forEach var="attribute" items="${requestScope.attributes}">
                        ${attribute.name}: <span class="blue">${attribute.valueAsString}</span><br/>
                    </c:forEach>
                    Price: <span class="blue">${product.price.value}</span><br/>
                </div>
                <page:addToCartButton product="${product}"/>
            </div>
        </div>
    </div>
</div>
