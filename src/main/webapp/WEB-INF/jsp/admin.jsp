<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:message key="title.adminPanel" var="titleMsg"/>
<page:genericPage title="${titleMsg}">
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
        </c:choose>
    </jsp:body>
</page:genericPage>
