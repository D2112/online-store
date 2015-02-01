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
            <c:when test="${cartMessage != null}">
                <page:message text="${cartMessage}"/>
            </c:when>
            <c:otherwise>
                <page:message text="Your shopping cart is empty"/>
            </c:otherwise>
        </c:choose>
    </jsp:body>
</page:genericPage>