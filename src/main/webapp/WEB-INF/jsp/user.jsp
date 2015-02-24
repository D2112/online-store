<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:message key="title.profile" var="titleMsg"/>
<page:genericPage title="${titleMsg}">
    <jsp:attribute name="leftSideBar">
        <page:userSideBar/>
    </jsp:attribute>
    <jsp:body>
        <page:findAndDisplayMessage/> <%--changing password succesMessage--%>
        <page:userPersonalData user="${sessionScope.user}"/>
        <a href="<c:url value="/user/change-password"/>">
            <h2><fmt:message key="profile.label.changePassword"/></h2>
        </a>
        <br/>
        <c:choose>
            <c:when test="${fn:length(orderList) > 0}">
                <h2><fmt:message key="profile.label.orderList"/>:<h2>
                <page:orderList orderList="${orderList}"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="profile.label.orderListIsEmpty"/>
            </c:otherwise>
        </c:choose>
    </jsp:body>
</page:genericPage>
