<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
            <c:when test="${successMessage != null}">
                <page:message text="${successMessage}"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="cart.message.emptyCart" var="emptyCartMessage"/>
                <page:message text="${emptyCartMessage}"/>
            </c:otherwise>
        </c:choose>
    </jsp:body>
</page:genericPage>