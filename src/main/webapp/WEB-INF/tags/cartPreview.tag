<%@ tag description="Writes HTML code to display cart's info box" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ attribute name="cart" type="com.epam.store.model.Cart" %>

<link rel="stylesheet" type="text/css" href="<c:url value="/static/css/style.css"/>"/>
<div class="shopping_cart">
    <div class="title_box">Shopping cart</div>
    <div class="cart_details">
        ${cart.productAmount} items<br/>
        <span class="border_cart"></span>
        Total: <span class="value"><page:price value="${cart.totalPrice}"/></span>
    </div>
    <div class="cart_icon"><a href="<c:url value="/cart"/>"><img src="../../static/img/shoppingcart.png" alt="" width="35" height="35"
                                               border="0"/></a></div>
</div>