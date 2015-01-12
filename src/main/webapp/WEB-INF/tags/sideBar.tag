<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ tag description="Writes the HTML code for inserting a right side bar."%>

<div class="right_content">
    <page:welcomeLabel user="${param.user}"/>
    <div class="border_box">
        <div class="shopping_cart">
            <div class="title_box">Shopping cart</div>
            <div class="cart_details"> 3 items <br />
                <span class="border_cart"></span> Total: <span class="value">350$</span> </div>
            <div class="cart_icon"><a href="#"><img src="../../images/shoppingcart.png" alt="" width="35" height="35" border="0" /></a></div>
        </div>
        <div class="title_box">Categories</div>
        <ul class="left_menu">
            <li class="even"><a href="catalog?category=guitars">Guitars</a></li>
            <li class="odd"><a href="#"></a></li>
        </ul>
    </div>
</div>