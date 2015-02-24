<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<fmt:message key="title.orders" var="titleMsg"/>
<page:genericPage title="${titleMsg}">
    <jsp:attribute name="leftSideBar">
        <page:adminSideBar/>
    </jsp:attribute>
    <jsp:body>
        <page:orderListForAdmin orderList="${requestScope.orderList}" user="${requestScope.user}"/>
        <c:if test="${fn:length(requestScope.orderList) == 0}">
            <h2 align="center"><fmt:message key="order-list.label.OrderListIsEmpty"/></h2>
        </c:if>
    </jsp:body>
</page:genericPage>
