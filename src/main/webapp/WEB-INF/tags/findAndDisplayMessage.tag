<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<c:choose>
    <c:when test="${not empty successMessage}">
        <page:message text="${successMessage}" success="true"/>
    </c:when>
    <c:when test="${not empty errorMessage}">
        <page:message text="${errorMessage}" error="true"/>
    </c:when>
    <c:when test="${not empty message}">
        <page:message text="${message}"/>
    </c:when>
</c:choose>