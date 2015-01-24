<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<page:genericPage>
    <jsp:attribute name="leftSideBar">
        <page:userSideBar/>
    </jsp:attribute>
    <jsp:body>
        <c:choose>
            <c:when test="${cart.productAmount != 0}">
                <page:cartInfoTable cart="${sessionScope.cart}"/>
            </c:when>
            <c:otherwise>
                <div class="center_text">
                    <h2>Your shopping cart is empty</h2>
                </div>
            </c:otherwise>
        </c:choose>
    </jsp:body>
</page:genericPage>