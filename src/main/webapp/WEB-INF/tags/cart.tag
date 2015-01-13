<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ tag description="Writes HTML code to display cart's info box" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="cart" type="com.epam.store.model.Cart" %>


<div class="shopping_cart">
    <div class="title_box">Shopping cart</div>
    <div class="cart_details">
        ${cart.productAmount()} items<br/>
        <span class="border_cart"></span>
        Total: <span class="value">${cart.totalPrice}</span>
    </div>
    <div class="cart_icon"><a href="#"><img src="../../static/img/shoppingcart.png" alt="" width="35" height="35"
                                            border="0"/></a></div>
</div>