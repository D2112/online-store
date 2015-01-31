<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<page:genericPage>
    <jsp:attribute name="leftSideBar">
        <page:adminSideBar/>
    </jsp:attribute>
    <jsp:body>
        <c:choose>
            <c:when test="${users != null}">
                <page:usersTable users="${users}"/>
            </c:when>
            <c:when test="${blackList != null}">
                <page:blackListTable blackList="${blackList}"/>
            </c:when>
            <c:when test="${orderList != null}">
                <page:purchaseList orderList="${orderList}"/>
            </c:when>
        </c:choose>
    </jsp:body>
</page:genericPage>
